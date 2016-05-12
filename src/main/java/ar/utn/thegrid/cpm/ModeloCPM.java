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
	private Nodo nodoInicial, nodoFinal = null;
	private HashMap<Integer, Tarea> tareas = new HashMap<>();
	private ArrayList<Nodo> nodos = new ArrayList<>();
	private int contadorNodos;

	public ModeloCPM() {
		contadorNodos = 1;
		this.nodoInicial = new Nodo(contadorNodos);
		nodos.add(nodoInicial);
	}

	public void agregarTarea(Tarea tarea) throws Exception {
		if (tarea == null) throw new Exception("La tarea no puede ser null");

		String precedencias = tarea.getPrecedencias();
		if (precedencias.isEmpty()) {
			tarea.setNodoOrigen(nodoInicial);
			contadorNodos++;
			Nodo nodoFin = new Nodo(contadorNodos);
			nodos.add(nodoFin);
			tarea.setNodoDestino(nodoFin);
		} else {
			ArrayList<Tarea> tareasPrecedentes = new ArrayList<>();
			for (String s : precedencias.split(",")) {
				Integer precedencia = Integer.valueOf(s);
				Tarea tareaPrecedente = tareas.get(precedencia);
				if (tareaPrecedente == null) {
					throw new Exception("La tarea tiene una precedencia no cargada");
				}
				tareasPrecedentes.add(tareaPrecedente);
			}
			for (Tarea tareaPrecedente : tareasPrecedentes) {
				Nodo nodoDestino = tareaPrecedente.getNodoDestino();
				if (nodoDestino.getTareasQueSalen().isEmpty()) {
					tareasPrecedentes.forEach(t -> {
						if (!t.getNodoDestino().equals(nodoDestino)) {
							nodos.remove(t.getNodoDestino());
						}
						t.setNodoDestino(nodoDestino);
					});
					break;
				}
			}
		}
		tareas.put(tarea.getNro(), tarea);
	}

	public ArrayList<Nodo> getNodos() {
		return nodos;
	}
}
