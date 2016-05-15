/**
 *
 */
package ar.utn.thegrid.cpm.visual;

import ar.utn.thegrid.cpm.modelo.Tarea;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

/** Wrapper para las tareas en la tabla
 *
 * @author eayzenberg
 */
public class FilaTarea {

	private Tarea tarea;
	private SimpleStringProperty propertyId;
	private SimpleDoubleProperty propertyDuracion;
	private SimpleStringProperty propertyPrecedencias;

	public FilaTarea(Tarea tarea) {
		this.tarea = tarea;
		propertyId = new SimpleStringProperty(tarea.getId());
		propertyDuracion = new SimpleDoubleProperty(tarea.getDuracion());
		propertyPrecedencias = new SimpleStringProperty(tarea.getPrecedencias());
	}

	public Tarea getTarea() {
		return tarea;
	}

	public SimpleStringProperty getPropertyId() {
		return propertyId;
	}

	public SimpleDoubleProperty getPropertyDuracion() {
		return propertyDuracion;
	}

	public SimpleStringProperty getPropertyPrecedencias() {
		return propertyPrecedencias;
	}
}
