/**
 * 
 */
package ar.utn.thegrid.cpm;

import java.util.ArrayList;

/**
 * Abstraccion de un nodo (conexion entre tareas)
 * 
 * @author eayzenberg
 *
 */
public class Nodo {
	private ArrayList<Tarea> tareasQueArriban = new ArrayList<Tarea>();
	private ArrayList<Tarea> tareasQueSalen = new ArrayList<Tarea>();
	
	public Nodo(int numeroNodo) {
		// TODO Auto-generated constructor stub
	}

	public ArrayList<Tarea> getTareasQueArriban() {
		return tareasQueArriban;
	}

	public ArrayList<Tarea> getTareasQueSalen() {
		return tareasQueSalen;
	}
}
