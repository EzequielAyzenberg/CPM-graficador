package ar.utn.thegrid.cpm;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class Main extends Application{

	private static final String RUTA_FXML = "controller.fxml";

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(this.getClass().getResource(RUTA_FXML));
		CPMController controller = new CPMController();
		loader.setController(controller);
		Pane pane = (Pane) loader.load();
		primaryStage.initStyle(StageStyle.DECORATED);
		primaryStage.setResizable(false);
		Scene scene = new Scene(pane, pane.getPrefWidth(), pane.getPrefHeight());
		primaryStage.setScene(scene);
		controller.start(primaryStage);
		primaryStage.show();
	}
}
