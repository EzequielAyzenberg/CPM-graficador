package ar.utn.thegrid.cpm;

import javafx.application.Application;
import javafx.stage.Stage;

public class MostrarModeloConUnaTarea extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	private CPMController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.controller = new CPMController();
		controller.start(primaryStage);
		primaryStage.show();
		controller.agregarTarea(new Tarea("A", 1.0, ""));
		controller.agregarTarea(new Tarea("B", 1.0, "A"));
		controller.agregarTarea(new Tarea("C", 1.0, ""));
		controller.generarEsquema();
	}
}
