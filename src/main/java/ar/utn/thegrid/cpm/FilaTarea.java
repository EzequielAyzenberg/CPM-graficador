/**
 * 
 */
package ar.utn.thegrid.cpm;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/** Wrapper para las tareas en la tabla
 *
 * @author eayzenberg
 */
public class FilaTarea {

	private Tarea tarea;
	private SimpleIntegerProperty propertyNro;
	private SimpleDoubleProperty propertyDuracion;
	private SimpleStringProperty propertyPrecedencias;

	public FilaTarea(Tarea tarea) {
		this.tarea = tarea;
		propertyNro = new SimpleIntegerProperty(tarea.getNro());
		propertyDuracion = new SimpleDoubleProperty(tarea.getDuracion());
		propertyPrecedencias = new SimpleStringProperty(tarea.getPrecedencias());
	}

	public Tarea getTarea() {
		return tarea;
	}

	public SimpleIntegerProperty getPropertyNro() {
		return propertyNro;
	}

	public SimpleDoubleProperty getPropertyDuracion() {
		return propertyDuracion;
	}

	public SimpleStringProperty getPropertyPrecedencias() {
		return propertyPrecedencias;
	}
}
