/**
 *
 */
package ar.utn.thegrid.cpm.dibujable;

import java.util.ArrayList;

import ar.utn.thegrid.cpm.modelo.Nodo;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**Parte grafica del nodo
 *
 * @author eayzenberg
 */
public class NodoDibujable {
	private ArrayList<TareaDibujable> tareasQueArriban = new ArrayList<>();
	private ArrayList<TareaDibujable> tareasQueSalen = new ArrayList<>();
	private Nodo nodo;
	private VBox contenedor;

	public NodoDibujable(Nodo nodoInterno) {
		nodo = nodoInterno;
		Label lblNro = new Label(nodoInterno.getNumeroNodo()+"");
		Label lblFechaTemprana = new Label(nodoInterno.getFechaTemprana()+"");
		lblFechaTemprana.setWrapText(true);
		Label lblFechaTardia = new Label(nodoInterno.getFechaTardia()+"");
		lblFechaTardia.setWrapText(true);
		HBox hBox = new HBox( lblFechaTemprana, lblFechaTardia);
		hBox.getStyleClass().add("hbox");
		contenedor = new VBox(10.0, lblNro, hBox);
		contenedor.getStyleClass().add("nodo");
		contenedor.getStyleClass().add("intermedio");
		contenedor.layoutXProperty().addListener((obs, o, n) -> nodo.setLayoutX(n));
		contenedor.layoutYProperty().addListener((obs, o, n) -> nodo.setLayoutY(n));
		contenedor.setLayoutX(nodoInterno.getLayoutX());
		contenedor.setLayoutY(nodoInterno.getLayoutY());
		if (nodoInterno.isNodoInicial()) {
			contenedor.getStyleClass().setAll("nodo", "inicial");
		} else if (nodoInterno.isNodoFinal()) {
			contenedor.getStyleClass().setAll("nodo", "final");
		} else if (nodoInterno.isCritico()) {
			contenedor.getStyleClass().setAll("nodo", "intermedio-critico");
		} else {
			contenedor.getStyleClass().setAll("nodo", "intermedio");
		}
	}

	public VBox getContenedor() {
		return contenedor;
	}

	public ArrayList<TareaDibujable> getTareasQueArriban() {
		return tareasQueArriban;
	}

	public ArrayList<TareaDibujable> getTareasQueSalen() {
		return tareasQueSalen;
	}
}
