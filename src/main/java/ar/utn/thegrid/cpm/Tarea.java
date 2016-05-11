/**
 *
 */
package ar.utn.thegrid.cpm;

import javafx.scene.Node;
import javafx.scene.paint.LinearGradient;
import javafx.scene.shape.Line;

/**
 * Abstraccion de una Tarea
 *
 * @author eayzenberg
 *
 */
public class Tarea {
	private Integer nro;
	private Double duracion;
	private String precedencias;
	private Nodo nodoOrigen, nodoDestino;
	private Line flecha = new Line();

	public Tarea(Integer nro, Double duracion, String precedencias) {
		this.setNro(nro);
		this.setDuracion(duracion);
		this.setPrecedencias(precedencias);
	}

	public Double getDuracion() {
		return duracion;
	}

	public void setDuracion(Double duracion) {
		this.duracion = duracion;
	}

	public String getPrecedencias() {
		return precedencias;
	}

	public void setPrecedencias(String precedencias) {
		this.precedencias = precedencias;
	}

	public Integer getNro() {
		return nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
	}

	public Nodo getNodoOrigen() {
		return nodoOrigen;
	}

	public void setNodoOrigen(Nodo nodo) {
		if (nodoOrigen != null)
			nodoOrigen.getTareasQueSalen().remove(this);
		this.nodoOrigen = nodo;
		nodo.getTareasQueSalen().add(this);
		flecha.startXProperty().unbind();
		flecha.startYProperty().unbind();
		flecha.startXProperty().bind(nodo.getContenedor().layoutXProperty().add(80));
		flecha.startYProperty().bind(nodo.getContenedor().layoutYProperty().add(40));
	}

	public Nodo getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(Nodo nodo) {
		if (nodoDestino != null)
			nodoDestino.getTareasQueArriban().remove(this);
		this.nodoDestino = nodo;
		nodo.getTareasQueArriban().add(this);
		flecha.endXProperty().unbind();
		flecha.endYProperty().unbind();
		flecha.endXProperty().bind(nodo.getContenedor().layoutXProperty());
		flecha.endYProperty().bind(nodo.getContenedor().layoutYProperty().add(40));
	}

	public Line getFlecha() {
		return flecha;
	}
}
