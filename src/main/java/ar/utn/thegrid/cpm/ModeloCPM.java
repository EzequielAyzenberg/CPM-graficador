/**
 *
 */
package ar.utn.thegrid.cpm;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Donde se realizan todos los calculos.
 *
 * @author ayzenberg
 */
public class ModeloCPM {
	private Nodo nodoInicial, nodoFinal;
	private HashMap<Integer, Tarea> tareas = new HashMap<>();
	private ArrayList<Nodo> nodos = new ArrayList<>();

	public ModeloCPM() {
		this.nodoInicial = new Nodo(1);
		this.nodoFinal = new Nodo(2);
		nodos.add(nodoInicial);
		nodos.add(nodoFinal);
	}

	public void agregarTarea(Tarea tarea) throws Exception {
		if (tarea == null) throw new Exception("La tarea no puede ser null");

		String precedencias = tarea.getPrecedencias();
		if (precedencias.isEmpty()) {
			tarea.setNodoOrigen(nodoInicial);
			tarea.setNodoDestino(nodoFinal);
		} else {
			for (String s : precedencias.split(",")) {
				Integer precedencia = Integer.valueOf(s);
				if (!tareas.containsKey(precedencia))
					throw new Exception("La tarea tiene una precedencia no cargada");
			}
		}
		tareas.put(tarea.getNro(), tarea);
	}

	public ArrayList<Nodo> getNodos() {
		return nodos;
	}
}
