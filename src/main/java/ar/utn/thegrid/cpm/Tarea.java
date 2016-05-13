/**
 *
 */
package ar.utn.thegrid.cpm;

import javafx.beans.binding.DoubleBinding;
import javafx.beans.property.DoubleProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Line;

/**
 * Abstraccion de una Tarea
 *
 * @author eayzenberg
 *
 */
public class Tarea {
	private String id;
	private Double duracion = 0.0;
	private String precedencias;
	private Nodo nodoOrigen, nodoDestino;
	private Line flecha = new Line();
	private int nroTareasDummy = 0;
	private Label id_lbl;
	private double margenLibre;
	private double margenTotal;
	private boolean critica;

	public Tarea(String id, Double duracion, String precedencias) {
		id_lbl = new Label();
		this.setId(id);
		this.setDuracion(duracion);
		this.setPrecedencias(precedencias);
	}

	public Double getDuracion() {
		return duracion;
	}

	public void setDuracion(Double duracion) {
		this.duracion = duracion;
		this.id_lbl.setText(id+"("+duracion+")");
	}

	public String getPrecedencias() {
		return precedencias;
	}

	public void setPrecedencias(String precedencias) {
		this.precedencias = precedencias;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
		this.id_lbl.setText(id+"("+duracion+")");
	}

	public Nodo getNodoOrigen() {
		return nodoOrigen;
	}

	public void setNodoOrigen(Nodo nodo) {
		if (nodoOrigen != null)
			nodoOrigen.getTareasQueSalen().remove(this);
		this.nodoOrigen = nodo;
		nodo.getTareasQueSalen().add(this);
		nodo.actualizarDescripcion();
	}

	public Nodo getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(Nodo nodo) {
		if (nodoDestino != null)
			nodoDestino.getTareasQueArriban().remove(this);
		this.nodoDestino = nodo;
		nodo.getTareasQueArriban().add(this);
		nodo.actualizarDescripcion();
	}

	public Line getFlecha() {
		return flecha;
	}

	public boolean esHoja() {
		return nodoDestino.getTareasQueArriban().size() == 1
			&& nodoDestino.getTareasQueSalen().isEmpty();
	}

	public int getNroTareasDummy() {
		return nroTareasDummy ;
	}

	public void setNroTareasDummy(int i) {
		nroTareasDummy = i;
	}

	@Override
	public String toString() {
		return "["+id+", "+duracion+", ("+precedencias+")]";
	}

	public void renderizarFlecha(AnchorPane lienzo) {
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
		lienzo.getChildren().add(id_lbl);
	}

	public void calcularMargenes() {
		margenLibre = nodoDestino.getFechaTemprana()
				- nodoOrigen.getFechaTemprana() - duracion;
		margenTotal = nodoDestino.getFechaTardia()
				- nodoOrigen.getFechaTemprana() - duracion;
		setCritica(margenLibre + margenTotal == 0);
	}

	public void setCritica(boolean critica) {
		this.critica = critica;
		if (critica) {
			flecha.getStyleClass().add("critica");
		} else {
			flecha.getStyleClass().removeAll("critica");
		}
	}

	public boolean isCritica() {
		return critica;
	}
}
