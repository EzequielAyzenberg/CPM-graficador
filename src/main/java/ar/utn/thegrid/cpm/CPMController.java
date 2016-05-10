package ar.utn.thegrid.cpm;

import java.io.File;

import javafx.fxml.FXML;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import jxl.Sheet;
import jxl.Workbook;

public class CPMController {
	
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

	private void cargarXls(File archivoXls) {
		try {
			Workbook archivoExcel = Workbook.getWorkbook(archivoXls);
			Sheet hoja = archivoExcel.getSheet(0);
			int numFilas = hoja.getRows();
			for (int fila = 1; fila < numFilas; fila++) { 
				// Recorre cada
				// fila de la
				// hoja
				Integer nro = Integer.getInteger(hoja.getCell(0, fila).getContents());
				Double duracion = Double.valueOf(hoja.getCell(1, fila).getContents());
				String precedencias = hoja.getCell(2, fila).getContents();
				Tarea tarea = new Tarea(nro, duracion, precedencias);
				tabla.getItems().add(new FilaTarea(tarea));
			}
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
	}

	public void start(Stage stage) {
		this.stage = stage;
		menuBtnImportar.setOnAction(e -> explorarXls());
		columnaNro.setCellValueFactory(f -> f.getValue().getPropertyNro());
		columnaDuracion.setCellValueFactory(f -> f.getValue().getPropertyDuracion());
		columnaPrecedencias.setCellValueFactory(f -> f.getValue().getPropertyPrecedencias());
	}

	private void explorarXls() {
		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("Importar archivo .xml");
		fileChooser.getExtensionFilters().add(new ExtensionFilter("Archivos .xls", ".xls"));
		File archivo = fileChooser.showOpenDialog(tabla.getScene().getWindow());
		if (archivo == null) return;
		cargarXls(archivo);
	}
}
