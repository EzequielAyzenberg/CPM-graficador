package ar.utn.thegrid.cpm;

import java.io.File;

import ar.utn.thegrid.cpm.modelo.Tarea;
import ar.utn.thegrid.cpm.visual.CPMController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ConstruirRedConCaminosRepetidos extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	private CPMController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.controller = new CPMController();
		controller.start(primaryStage);
		primaryStage.show();
		controller.agregarTarea(new Tarea("1", 34.0, ""));
		controller.agregarTarea(new Tarea("2", 56.0, "1"));
		controller.agregarTarea(new Tarea("3", 34.0, "1"));
		controller.agregarTarea(new Tarea("4", 23.0, "2,3"));
		controller.agregarTarea(new Tarea("5", 12.0, "4"));
		controller.agregarTarea(new Tarea("6", 7.0, "4,1"));
		controller.generarEsquema();
		controller.getModelo().persistir(new File("asd.xml"));
	}
}
