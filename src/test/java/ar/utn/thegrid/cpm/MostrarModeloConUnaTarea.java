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
		controller.agregarTarea(new Tarea(0, 1.0, ""));
	}
}
