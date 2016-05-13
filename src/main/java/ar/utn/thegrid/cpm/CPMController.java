package ar.utn.thegrid.cpm;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
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
	private TableView<FilaTarea> tabla;
	@FXML
	private TableColumn<FilaTarea, String> columnaNro;
	@FXML
	private TableColumn<FilaTarea, Number> columnaDuracion;
	@FXML
	private TableColumn<FilaTarea, String> columnaPrecedencias;

	private Stage stage;
	private ModeloCPM modelo;

	private Double deltaY;

	private Double deltaX;

	public CPMController() {
		this.modelo = new ModeloCPM();
	}

	private void cargarXls(File archivoXls) {
		try {
			Workbook archivoExcel = Workbook.getWorkbook(archivoXls);
			Sheet hoja = archivoExcel.getSheet(0);
			int numFilas = hoja.getRows();
			for (int fila = 1; fila < numFilas; fila++) {
				// Recorre cada fila de la hoja
				String id = hoja.getCell(0, fila).getContents();
				Double duracion = Double.valueOf(hoja.getCell(1, fila).getContents());
				String precedencias = hoja.getCell(2, fila).getContents();
				Tarea tarea = new Tarea(id, duracion, precedencias);
				tabla.getItems().add(new FilaTarea(tarea));
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
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
		stage.setResizable(true);
		stage.setMaximized(true);
		menuBtnImportar.setOnAction(e -> explorarXls());
		columnaNro.setCellValueFactory(f -> f.getValue().getPropertyId());
		columnaDuracion.setCellValueFactory(f -> f.getValue().getPropertyDuracion());
		columnaPrecedencias.setCellValueFactory(f -> f.getValue().getPropertyPrecedencias());
	}

	public void generarEsquema() {
		modelo.generarNodoFinal();
		modelo.calcularFechas();
		modelo.calcularMargenes();
		modelo.calcularIntervalosDeFlotamiento();
		Nodo nodoInicial = modelo.getNodoInicial();
		deltaY = 100.0;
		deltaX = 80.0;
		ArrayList<Nodo> nodos = modelo.getNodos();
		Collection<Tarea> tareas = modelo.getTareas().values();
		@SuppressWarnings("unchecked")
		ArrayList<Nodo>[] posiciones = new ArrayList[nodos.size()];
		for (int i=0;i<nodos.size();i++) {
			posiciones[i] = new ArrayList<>();
			nodos.get(i).setNumeroNodo(i+1);
		}
		nodos.forEach(n -> lienzo.getChildren().add(n.getContenedor()));
		tareas.forEach(t -> lienzo.getChildren().add(t.getFlecha()));
		disponerEnEsquema(nodoInicial,0,posiciones);
		int maxPos = 0;
		int maxProfundidad = 0;
		for (maxPos = 0; posiciones[maxPos].size() > 0; maxPos++){
			int profundidad = posiciones[maxPos].size();
			if (profundidad > maxProfundidad)
				maxProfundidad = profundidad;
		}
		lienzo.setPrefSize((deltaX+50)*maxPos, (deltaY+50)*maxProfundidad);
		tareas.forEach(t -> t.renderizarFlecha(lienzo));
	}

	private void disponerEnEsquema(Nodo nodo, int nivel, ArrayList<Nodo>[] nodosNivelados) {
		ArrayList<Nodo> nodosDelNivel = nodosNivelados[nivel];
		if (nodosDelNivel.contains(nodo)) return;
		nodosDelNivel.add(nodo);
		VBox contenedor = nodo.getContenedor();
		contenedor.setLayoutY((nodosDelNivel.size()-1)*(deltaY)+20);
		contenedor.setLayoutX(nivel*(deltaX+50)+50);
		cargarlisteners(nodo);
		for (Tarea tarea : nodo.getTareasQueSalen()) {
			disponerEnEsquema(tarea.getNodoDestino(), nivel+1, nodosNivelados);
		}
	}

	private void cargarlisteners(Nodo nodo) {
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
