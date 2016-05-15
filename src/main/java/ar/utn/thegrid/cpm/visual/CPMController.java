package ar.utn.thegrid.cpm.visual;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import javax.imageio.ImageIO;

import ar.utn.thegrid.cpm.dibujable.NodoDibujable;
import ar.utn.thegrid.cpm.dibujable.TareaDibujable;
import ar.utn.thegrid.cpm.modelo.ModeloCPM;
import ar.utn.thegrid.cpm.modelo.Nodo;
import ar.utn.thegrid.cpm.modelo.Tarea;
import javafx.collections.FXCollections;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import jxl.Sheet;
import jxl.Workbook;

public class CPMController {

	private static final String RUTA_FXML = "controller.fxml";

	@FXML
	private AnchorPane lienzo;
	@FXML
	private MenuItem menuBtnImportar;
	@FXML
	private MenuItem menuBtnGuardar;
	@FXML
	private MenuItem menuBtnAbrir;
	@FXML
	private MenuItem menuBtnExportar;
	@FXML
	private TableView<FilaTarea> tabla;
	@FXML
	private TableColumn<FilaTarea, String> columnaNro;
	@FXML
	private TableColumn<FilaTarea, Number> columnaDuracion;
	@FXML
	private TableColumn<FilaTarea, String> columnaPrecedencias;

	private Stage stage;
	private ModeloCPM modelo;
	private Double deltaY = 100.0, deltaX = 80.0;
	private HashMap<Integer, NodoDibujable> nodosDibujables= new HashMap<Integer, NodoDibujable>();
	private ArrayList<TareaDibujable> tareas = new ArrayList<>();

	public CPMController() {
		this.modelo = new ModeloCPM();
	}

	private void cargarXls(File archivoXls) {
		limpiarLienzo();
		this.modelo = new ModeloCPM();
		Workbook archivoExcel;
		try {
			archivoExcel = Workbook.getWorkbook(archivoXls);
			Sheet hoja = archivoExcel.getSheet(0);
			int numFilas = hoja.getRows();
			ArrayList<String> idsTarea = new ArrayList<>();
			for (int fila = 1; fila < numFilas; fila++) {
				// Recorre cada fila de la hoja
				String id = hoja.getCell(0, fila).getContents();
				if (id.isEmpty()) {
					throw new Exception("Fila "+(fila+1)+" malformada. El id no puede estar vacio");
				}

				Double duracion = null;
				try {
					duracion = Double.valueOf(hoja.getCell(1, fila).getContents());
				} catch (Exception e) {
					throw new Exception("Fila "+(fila+1)+" malformada. Duracion invalida");
				}

				String precedencias = hoja.getCell(2, fila).getContents();

				idsTarea.add(id);
				for (String precedencia : precedencias.split(",")) {
					if (precedencia.isEmpty()) continue;
					if (precedencia == id) {
						throw new Exception("Fila "+(fila+1)+" malformada. "
								+ "No puede tener una precedencia a si misma");
					}
					if (!idsTarea.contains(precedencia)) {
						throw new Exception("Fila "+(fila+1)+" malformada. "+
								"Tiene precedencias inexistentes. \n\n"
										+ "No existe la tarea "+precedencia
										+ "\nRecuerda que las tareas deben venir en orden.");
					}
				}

				agregarTarea(new Tarea(id, duracion, precedencias));
			}
			menuBtnGuardar.setDisable(false);
			agregarTareasATabla();
			generarEsquema();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error en la carga de datos");
			alert.setContentText(e.getMessage());
			alert.showAndWait();
			e.printStackTrace();
			modelo.getTareas().clear();
		}
	}

	public void start(Stage primaryStage) throws IOException {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(RUTA_FXML));
		loader.setController(this);
		Pane pane = (Pane) loader.load();
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.setResizable(false);
		Scene scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight());
		primaryStage.setScene(scene);
		this.stage = primaryStage;
		stage.setTitle("TheGrid - Graficador CPM");
		stage.setResizable(true);
		stage.setMaximized(true);
		menuBtnImportar.setOnAction(e -> explorarXls());
		menuBtnGuardar.setOnAction(e -> guardar());
		menuBtnAbrir.setOnAction(e -> abrirXML());
		menuBtnExportar.setOnAction(e -> exportarJPG());
		columnaNro.setCellValueFactory(f -> f.getValue().getPropertyId());
		columnaDuracion.setCellValueFactory(f -> f.getValue().getPropertyDuracion());
		columnaPrecedencias.setCellValueFactory(f -> f.getValue().getPropertyPrecedencias());
		tabla.setSortPolicy(t -> {
		    Comparator<FilaTarea> comparator = (r1, r2)
		            -> r1.getTarea().getId().compareTo(r2.getTarea().getId());
		    FXCollections.sort(tabla.getItems(), comparator);
		    return true;
		});
	}

	private void exportarJPG() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Exportar a png");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos .png", "*.png"));
		File archivo = fileChooser.showSaveDialog(tabla.getScene().getWindow());
		if (archivo == null) return;
		try {
			int height = new Double(lienzo.getPrefHeight()).intValue();
			int width = new Double(lienzo.getPrefWidth()).intValue();
			WritableImage writableImage = new WritableImage(width, height);
			lienzo.snapshot(new SnapshotParameters(), writableImage);
			RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
			ImageIO.write(renderedImage, "png", archivo);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar .png");
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	private void abrirXML() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Abrir proyecto");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos .xml", "*.xml"));
		File archivo = fileChooser.showOpenDialog(tabla.getScene().getWindow());
		if (archivo == null) return;
		try {
			modelo = ModeloCPM.recuperar(archivo);
			renderizarEsquema();
			dimensionesLienzo();
			agregarTareasATabla();
		} catch (Exception e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al abrir .xml");
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	public void agregarTareasATabla() {
		tabla.getItems().clear();
		for (Tarea tarea : modelo.getTareas().values()) {
			tabla.getItems().add(new FilaTarea(tarea));
		}
		tabla.sort();
	}

	private void guardar() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Guardar proyecto");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos .xml", "*.xml"));
		File archivo = fileChooser.showSaveDialog(tabla.getScene().getWindow());
		if (archivo == null) return;
		try {
			modelo.persistir(archivo);
		} catch (IOException e) {
			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error al guardar .xml");
			alert.showAndWait();
			e.printStackTrace();
		}
	}

	private void renderizarEsquema() {
		menuBtnGuardar.setDisable(false);
		menuBtnExportar.setDisable(false);
		limpiarLienzo();
		generarDibujables();
	}

	private void limpiarLienzo() {
		lienzo.getChildren().clear();
		tareas.clear();
		nodosDibujables.clear();
	}

	public void generarEsquema() {
		modelo.generarNodoFinal();
		modelo.calcularFechas();
		modelo.calcularMargenes();
		modelo.calcularIntervalosDeFlotamiento();
		@SuppressWarnings("unchecked")
		ArrayList<NodoDibujable>[] posiciones = new ArrayList[modelo.getTareas().size()+1];
		for (int i=0;i<posiciones.length;i++) {
			posiciones[i] = new ArrayList<>();
		}

		renderizarEsquema();

		NodoDibujable nodoInicial = nodosDibujables.get(modelo.getNodoInicial().getNumeroNodo());
		disponerEnEsquema(nodoInicial,0,posiciones);
		int maxPos = 0;
		int maxProfundidad = 0;
		for (maxPos = 0; posiciones[maxPos].size() > 0; maxPos++){
			int profundidad = posiciones[maxPos].size();
			if (profundidad > maxProfundidad)
				maxProfundidad = profundidad;
		}

		double anchoLienzo = (deltaX+50)*maxPos;
		double altoLienzo = (deltaY+50)*maxProfundidad;
		modelo.setAnchoLienzo(anchoLienzo);
		modelo.setAltoLienzo(altoLienzo);
		dimensionesLienzo();
	}

	private void dimensionesLienzo() {
		lienzo.setPrefSize(modelo.getAnchoLienzo(), modelo.getAltoLienzo());
	}

	private void generarDibujables() {
		ArrayList<Nodo> nodos = modelo.getNodos();
		for (int i = 0; i < nodos.size(); i++) {
			Nodo nodo = nodos.get(i);
			nodo.setNumeroNodo(i+1);
			NodoDibujable nodoDibujable = new NodoDibujable(nodo);
			nodosDibujables.put(nodo.getNumeroNodo(), nodoDibujable);
			lienzo.getChildren().add(nodoDibujable.getContenedor());
		}
		for (Tarea tarea : modelo.getTareas().values()) {
			TareaDibujable tareaDibujable = new TareaDibujable(tarea);
			tareas.add(tareaDibujable);
			lienzo.getChildren().addAll(tareaDibujable.getFlecha(), tareaDibujable.getLabelId());
			tareaDibujable.setNodoDestino(nodosDibujables.get(tarea.getNodoDestino().getNumeroNodo()));
			tareaDibujable.setNodoOrigen(nodosDibujables.get(tarea.getNodoOrigen().getNumeroNodo()));
			tareaDibujable.renderizarFlecha();
		}
	}

	private void disponerEnEsquema(NodoDibujable nodo, int nivel, ArrayList<NodoDibujable>[] nodosNivelados) {
		ArrayList<NodoDibujable> nodosDelNivel = nodosNivelados[nivel];
		if (nodosDelNivel.contains(nodo)) return;
		nodosDelNivel.add(nodo);
		VBox contenedor = nodo.getContenedor();
		contenedor.setLayoutY((nodosDelNivel.size()-1)*(deltaY)+20);
		contenedor.setLayoutX(nivel*(deltaX+50)+50);
		cargarlisteners(nodo);
		for (TareaDibujable tarea : nodo.getTareasQueSalen()) {
			disponerEnEsquema(tarea.getNodoDestino(), nivel+1, nodosNivelados);
		}
	}

	private void cargarlisteners(NodoDibujable nodo) {
		new CargadorListenersDragAndDrop(nodo, lienzo);
	}

	private void explorarXls() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Importar archivo .xml");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos .xls", "*.xls"));
		File archivo = fileChooser.showOpenDialog(tabla.getScene().getWindow());
		if (archivo == null) return;
		cargarXls(archivo);
	}

	public ModeloCPM getModelo() {
		return modelo;
	}

	public void agregarTarea(Tarea tarea) {
		try {
			modelo.agregarTarea(tarea);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
