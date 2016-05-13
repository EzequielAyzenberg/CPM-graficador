/**
 *
 */
package ar.utn.thegrid.cpm;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

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
	private Double fechaTemprana = null;
	private Double fechaTardia = null;
	private int numeroNodo;
	private VBox contenedor;
	private Label lblNro;
	private Label lblFechaTemprana;
	private Label lblFechaTardia;
	private String tareasQueArriban_s, tareasQueSalen_s;
	private double intervaloFlotamiento;
	private boolean esInicial;
	private boolean esFinal;

	public Nodo() {
		lblNro = new Label();
		lblFechaTemprana = new Label();
		lblFechaTemprana.setWrapText(true);
		lblFechaTardia = new Label();
		lblFechaTardia.setWrapText(true);
		HBox hBox = new HBox( lblFechaTemprana, lblFechaTardia);
		hBox.getStyleClass().add("hbox");
		contenedor = new VBox(10.0, lblNro, hBox);
		contenedor.getStyleClass().add("nodo");
		contenedor.getStyleClass().add("intermedio");
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
		lblFechaTardia.setText(fechaTardia+"");
	}

	public Double getFechaTemprana() {
		return fechaTemprana;
	}

	public void setFechaTemprana(Double fechaTemprana) {
		this.fechaTemprana = fechaTemprana;
		lblFechaTemprana.setText(fechaTemprana+"");
	}

	public int getNumeroNodo() {
		return numeroNodo;
	}

	public void setNumeroNodo(int numeroNodo) {
		this.numeroNodo = numeroNodo;
		lblNro.setText(numeroNodo+"");
	}

	public VBox getContenedor() {
		return contenedor;
	}

	public void actualizarDescripcion() {
		tareasQueArriban_s = StringUtils.join(
				tareasQueArriban.stream().map(t -> t.getId())
						.collect(Collectors.toList()), ",");
		tareasQueSalen_s = StringUtils.join(
				tareasQueSalen.stream().map(t -> t.getId())
						.collect(Collectors.toList()), ",");
	}

	@Override
	public String toString() {
		return "(" + tareasQueArriban_s + ") => (" + tareasQueSalen_s + ")";
	}

	public void settearComoNodoInicial() {
		setFechaTardia(0.0);
		setFechaTemprana(0.0);
		contenedor.getStyleClass().setAll("nodo", "inicial");
		esInicial = true;
	}

	public void settearComoNodoFinal() {
		setFechaTardia(fechaTemprana);
		contenedor.getStyleClass().setAll("nodo", "final");
		esFinal = true;
	}

	public void calcularIntervaloDeFlotamiento() {
		intervaloFlotamiento = fechaTardia - fechaTemprana;
		if (!esInicial && !esFinal) {
			setCritico(intervaloFlotamiento==0);
		}
	}

	public void setCritico(boolean critico) {
		if (critico) {
			contenedor.getStyleClass().setAll("nodo", "intermedio-critico");
		} else {
			contenedor.getStyleClass().setAll("nodo", "intermedio");
		}
	}
}
