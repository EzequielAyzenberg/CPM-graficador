package ar.utn.thegrid.cpm;

import java.io.File;
import java.io.IOException;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.DragEvent;
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
	private TableColumn<FilaTarea, Number> columnaNro;
	@FXML
	private TableColumn<FilaTarea, Number> columnaDuracion;
	@FXML
	private TableColumn<FilaTarea, String> columnaPrecedencias;

	private Stage stage;
	private ModeloCPM modelo;

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
				Integer nro = Integer.valueOf(hoja.getCell(0, fila).getContents());
				Double duracion = Double.valueOf(hoja.getCell(1, fila).getContents());
				String precedencias = hoja.getCell(2, fila).getContents();
				Tarea tarea = new Tarea(nro, duracion, precedencias);
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
		menuBtnImportar.setOnAction(e -> explorarXls());
		columnaNro.setCellValueFactory(f -> f.getValue().getPropertyNro());
		columnaDuracion.setCellValueFactory(f -> f.getValue().getPropertyDuracion());
		columnaPrecedencias.setCellValueFactory(f -> f.getValue().getPropertyPrecedencias());

		Double posH = 50.0;
		for (Nodo nodo : modelo.getNodos()) {
			VBox contenedor = nodo.getContenedor();
			lienzo.getChildren().add(contenedor);
			contenedor.setLayoutX(posH);
			contenedor.setLayoutY(25);
			posH+=80+50;
			cargarlisteners(nodo);
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
			lienzo.getChildren().add(tarea.getFlecha());
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
