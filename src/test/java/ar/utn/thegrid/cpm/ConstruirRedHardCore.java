package ar.utn.thegrid.cpm;

import ar.utn.thegrid.cpm.modelo.Tarea;
import ar.utn.thegrid.cpm.visual.CPMController;
import javafx.application.Application;
import javafx.stage.Stage;

public class ConstruirRedHardCore extends Application{

	public static void main(String[] args) {
		launch(args);
	}

	private CPMController controller;

	@Override
	public void start(Stage primaryStage) throws Exception {
		this.controller = new CPMController();
		controller.start(primaryStage);
		primaryStage.show();
		controller.agregarTarea(new Tarea("A", 4.0, ""));
		controller.agregarTarea(new Tarea("B", 7.0, ""));
		controller.agregarTarea(new Tarea("C", 2.0, ""));
		controller.agregarTarea(new Tarea("D", 3.0, "A"));
		controller.agregarTarea(new Tarea("E", 2.0, "A"));
		controller.agregarTarea(new Tarea("F", 5.0, "A,C"));
		controller.agregarTarea(new Tarea("G", 8.0, "D"));
		controller.agregarTarea(new Tarea("H", 7.0, "B,E,F"));
		controller.agregarTarea(new Tarea("I", 10.0, "B,E,F"));
		controller.agregarTarea(new Tarea("J", 13.0, "F"));
		controller.agregarTarea(new Tarea("K", 6.0, "G,H"));
		controller.agregarTarea(new Tarea("L", 15.0, "G,H"));
		controller.agregarTarea(new Tarea("M", 9.0, "I"));
		controller.agregarTarea(new Tarea("N", 8.0, "I,J"));
		controller.agregarTarea(new Tarea("O", 5.0, "K,I"));
		controller.agregarTarea(new Tarea("P", 4.0, "L,M,N"));
		controller.generarEsquema();
		controller.agregarTareasATabla();
	}
}
