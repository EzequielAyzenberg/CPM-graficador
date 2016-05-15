/**
 *
 */
package ar.utn.thegrid.cpm.dibujable;

import ar.utn.thegrid.cpm.modelo.Tarea;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.shape.Line;

/**Parte grafica de una tarea.
 *
 * @author eayzenberg
 */
public class TareaDibujable {
	private Label id_lbl = new Label();
	private Line flecha = new Line();
	private NodoDibujable nodoOrigen, nodoDestino;
	private Tarea tarea;

	public TareaDibujable(Tarea tarea) {
		this.tarea = tarea;
		id_lbl.setText(tarea.getId()+"("+tarea.getDuracion()+")");
		if (tarea.isCritica()) {
			flecha.getStyleClass().add("critica");
			id_lbl.setStyle("-fx-text-fill: red");
		}
		if (tarea.esDummy()) {
			flecha.getStrokeDashArray().addAll(25d, 10d);
		}
	}

	public Line getFlecha() {
		return flecha;
	}

	public void renderizarFlecha() {
		DoubleBinding startX = nodoOrigen.getContenedor().layoutXProperty().add(102);
		DoubleBinding startY = nodoOrigen.getContenedor().layoutYProperty().add(40);
		DoubleProperty endX = nodoDestino.getContenedor().layoutXProperty();
		DoubleBinding endY = nodoDestino.getContenedor().layoutYProperty().add(40);
		flecha.startXProperty().bind(startX);
		flecha.startYProperty().bind(startY);
		flecha.endXProperty().bind(endX);
		flecha.endYProperty().bind(endY);
		startX.addListener((obs, o, n) -> id_lbl.setLayoutX(((double)n + endX.get())/2+10));
		endX.addListener((obs, o, n) -> id_lbl.setLayoutX(((double)n + startX.get())/2+10));
		startY.addListener((obs, o, n) -> id_lbl.setLayoutY(((double)n + endY.get())/2+10));
		endY.addListener((obs, o, n) -> id_lbl.setLayoutY(((double)n + startY.get())/2+10));
		id_lbl.setLayoutX((startX.get() + endX.get())/2);
		id_lbl.setLayoutY((startY.get() + endY.get())/2);
	}

	public NodoDibujable getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(NodoDibujable nodo) {
		if (nodoDestino != null)
			nodoDestino.getTareasQueArriban().remove(this);
		this.nodoDestino = nodo;
		nodo.getTareasQueArriban().add(this);
	}

	public void setNodoOrigen(NodoDibujable nodo) {
		if (nodoOrigen != null)
			nodoOrigen.getTareasQueSalen().remove(this);
		this.nodoOrigen = nodo;
		nodo.getTareasQueSalen().add(this);
	}

	@Override
	public String toString() {
		return tarea.toString();
	}

	public Node getLabelId() {
		return id_lbl;
	}

	public Tarea getTarea() {
		return tarea;
	}
}
