/**
 *
 */
package ar.utn.thegrid.cpm.modelo;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;

/**
 * Abstraccion de un nodo (conexion entre tareas)
 *
 * @author eayzenberg
 */
public class Nodo {
	private ArrayList<Tarea> tareasQueArriban = new ArrayList<Tarea>();
	private ArrayList<Tarea> tareasQueSalen = new ArrayList<Tarea>();
	private Double fechaTemprana = null;
	private Double fechaTardia = null;
	private int numeroNodo;
	private String tareasQueArriban_s, tareasQueSalen_s;
	private double intervaloFlotamiento;
	private boolean esInicial;
	private boolean esFinal;
	private double layoutX, layoutY;
	private boolean critico;

	public Nodo() {
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
		esInicial = true;
	}

	public void settearComoNodoFinal() {
		setFechaTardia(fechaTemprana);
		esFinal = true;
	}

	public void calcularIntervaloDeFlotamiento() {
		intervaloFlotamiento = fechaTardia - fechaTemprana;
		if (!esInicial && !esFinal) {
			setCritico(intervaloFlotamiento==0);
		}
	}

	public void setCritico(boolean critico) {
		this.critico = critico;
	}

	public void setLayoutX(Number n) {
		layoutX = (Double) n;
	}

	public void setLayoutY(Number n) {
		layoutY = (Double) n;
	}

	public boolean isNodoInicial() {
		return esInicial;
	}

	public boolean isNodoFinal() {
		return esFinal;
	}

	public boolean isCritico() {
		return critico;
	}

	public double getLayoutX() {
		return layoutX;
	}

	public double getLayoutY() {
		return layoutY;
	}
}
