/**
 *
 */
package ar.utn.thegrid.cpm;

import java.util.ArrayList;

import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * Abstraccion de un nodo (conexion entre tareas)
 *
 * @author eayzenberg
 *
 */
public class Nodo {
	private ArrayList<Tarea> tareasQueArriban = new ArrayList<Tarea>();
	private ArrayList<Tarea> tareasQueSalen = new ArrayList<Tarea>();
	private Double fechaTemprana;
	private Double fechaTardia;
	private int numeroNodo;
	private VBox contenedor;
	private Label lblNro;
	private Label lblFechaTemprana;
	private Label lblFechaTardia;

	public Nodo() {
		lblNro = new Label();
		lblFechaTemprana = new Label();
		lblFechaTardia = new Label();
		HBox hBox = new HBox(30, lblFechaTemprana, lblFechaTardia);
		contenedor = new VBox(10.0, lblNro, hBox);
		contenedor.getStyleClass().add("nodo");
	}

	public ArrayList<Tarea> getTareasQueArriban() {
		return tareasQueArriban;
	}

	public ArrayList<Tarea> getTareasQueSalen() {
		return tareasQueSalen;
	}

	public Double getFechaTardia() {
		return fechaTardia;
	}

	public void setFechaTardia(Double fechaTardia) {
		this.fechaTardia = fechaTardia;
	}

	public Double getFechaTemprana() {
		return fechaTemprana;
	}

	public void setFechaTemprana(Double fechaTemprana) {
		this.fechaTemprana = fechaTemprana;
	}

	public int getNumeroNodo() {
		return numeroNodo;
	}

	public void setNumeroNodo(int numeroNodo) {
		this.numeroNodo = numeroNodo;
	}

	public VBox getContenedor() {
		return contenedor;
	}
}
