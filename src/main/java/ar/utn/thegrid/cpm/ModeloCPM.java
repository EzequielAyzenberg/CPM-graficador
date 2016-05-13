/**
 *
 */
package ar.utn.thegrid.cpm;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.stream.Collectors;

/**
 * Donde se realizan todos los calculos.
 *
 * @author ayzenberg
 */
public class ModeloCPM {
	private Nodo nodoInicial;
	private HashMap<String, Tarea> tareas = new HashMap<>();
	private ArrayList<Nodo> nodos = new ArrayList<>();
	private Nodo nodoFinal;

	public ModeloCPM() {
		nodoInicial = new Nodo();
		nodoInicial.setNumeroNodo(0);
		nodos.add(nodoInicial);
	}

	public void agregarTarea(Tarea tarea) throws Exception {
		if (tarea == null) throw new Exception("La tarea no puede ser null");

		if (tarea.getPrecedencias().isEmpty()) {
			agregarComoInicial(tarea);
		} else {
			agregarComoIntermedia(tarea);

		}
		tareas.put(tarea.getId(), tarea);
	}

	private void agregarComoIntermedia(Tarea tarea) throws Exception {
		ArrayList<Tarea> precedentes = obtenerPrecedentes(tarea);
		normalizarNodos(precedentes);

		if (precedentes.size()==1) {
			Nodo nodoOrigen = precedentes.get(0).getNodoDestino();
			tarea.setNodoOrigen(nodoOrigen);
			crearNuevoNodoFinalParaTarea(tarea);
			return;
		}

		Tarea precedenteApoyo = elegirTareaDeApoyo(precedentes);
		juntarTareasSiEsPosible(precedenteApoyo, precedentes);
		tarea.setNodoOrigen(precedenteApoyo.getNodoDestino());
		crearNuevoNodoFinalParaTarea(tarea);
	}

	private void juntarTareasSiEsPosible(Tarea precedenteApoyo, ArrayList<Tarea> precedentes) {
		// Elimino nodos redundantes. Si me propongo a establecer
		// una nueva tarea con 3 tareas precedentes y todas ellas son hojas,
		// junto las hojas en un solo nodo, siempre que sea posible.
		for (Tarea precedente : precedentes) {
			if (precedente == precedenteApoyo) continue;
			if (precedente.getNodoDestino().equals(precedenteApoyo.getNodoDestino())) continue;
			if (precedente.esHoja()) {
				nodos.remove(precedente.getNodoDestino());
				precedente.setNodoDestino(precedenteApoyo.getNodoDestino());
			} else {
				// El nodo tiene mas de una tarea entrante,
				// se puentean los nodos con una dummy.
				TareaDummy dummy = new TareaDummy();
				dummy.setNodoOrigen(precedente.getNodoDestino());
				dummy.setNodoDestino(precedenteApoyo.getNodoDestino());
				int nroTareasDummy = precedente.getNroTareasDummy();
				nroTareasDummy++;
				dummy.setId(precedente.getId()+"."+nroTareasDummy);
				precedente.setNroTareasDummy(nroTareasDummy);
				tareas.put(dummy.getId(), dummy);
			}
		}
	}

	private void crearNuevoNodoFinalParaTarea(Tarea tarea) {
		Nodo nodoFin = new Nodo();
		nodos.add(nodoFin);
		tarea.setNodoDestino(nodoFin);
	}

	private Tarea elegirTareaDeApoyo(ArrayList<Tarea> precedentes) {
		// Itero. Me interesa como apoyo una tarea hoja.
		// Sino me quedo con cualquiera.
		for (Tarea tarea : precedentes) {
			if (tarea.getNodoDestino().getTareasQueSalen().isEmpty()) {
				return tarea;
			}
		}
		return precedentes.get(0);
	}

	private void normalizarNodos(ArrayList<Tarea> tareasPrecedentes) {
		// Si hay tareas que tienen nodos destino con precedencias que
		// no quiero, debo crear tareas dummy para separar nodos
		for (Tarea tarea : tareasPrecedentes) {
			Nodo nodoDestino = tarea.getNodoDestino();
			for (Tarea tareaDelNodo : nodoDestino.getTareasQueArriban()) {
				if (!tareasPrecedentes.contains(tareaDelNodo)) {
					// Hay una tarea que no es precedente :-(
					crearTareaDummy(tarea);
					break;
				}
			}
		}
	}

	private void crearTareaDummy(Tarea tarea) {
		TareaDummy tareaDummy = new TareaDummy();
		tareaDummy.extenderTarea(tarea);
		nodos.add(tareaDummy.getNodoOrigen());
		tareas.put(tareaDummy.getId(), tareaDummy);
	}

	private ArrayList<Tarea> obtenerPrecedentes(Tarea tarea) throws Exception {
		ArrayList<Tarea> tareasPrecedentes;
		tareasPrecedentes = new ArrayList<>();
		for (String precedencia : tarea.getPrecedencias().split(",")) {
			Tarea tareaPrecedente = tareas.get(precedencia);
			if (tareaPrecedente == null) {
				throw new Exception("La tarea tiene una precedencia no cargada");
			}
			tareasPrecedentes.add(tareaPrecedente);
		}
		return tareasPrecedentes;
	}

	private void agregarComoInicial(Tarea tarea) {
		tarea.setNodoOrigen(nodoInicial);
		crearNuevoNodoFinalParaTarea(tarea);
	}

	public ArrayList<Nodo> getNodos() {
		return nodos;
	}

	public HashMap<String, Tarea> getTareas() {
		return tareas;
	}

	public void generarNodoFinal() {
		ArrayList<Tarea> finales = (ArrayList<Tarea>) tareas.values().stream()
				.filter(t -> t.esHoja()).collect(Collectors.toList());
		if (finales.isEmpty()) return;
		Tarea pivote = finales.get(0);
		juntarTareasSiEsPosible(pivote, finales);
	}

	public Nodo getNodoInicial() {
		return nodoInicial;
	}

	public void calcularFechas() {
		nodoInicial.settearComoNodoInicial();
		calcularFechasTempranas(nodoInicial);
		nodoFinal.settearComoNodoFinal();
		calcularFechasTardias(nodoFinal);
	}

	private void calcularFechasTardias(Nodo nodo) {
		ArrayList<Tarea> tareasQueArriban = nodo.getTareasQueArriban();
		if (tareasQueArriban.isEmpty()) {
			return;
		}
		for (Tarea tarea : tareasQueArriban) {
			Double desfasaje = nodo.getFechaTardia() - tarea.getDuracion();
			Nodo nodoOrigen = tarea.getNodoOrigen();
			if (nodoOrigen.getFechaTardia() != null &&
				nodoOrigen.getFechaTardia() < desfasaje) continue;
			nodoOrigen.setFechaTardia(desfasaje);
			calcularFechasTardias(nodoOrigen);
		}
	}

	private void calcularFechasTempranas(Nodo nodo) {
		ArrayList<Tarea> tareasQueSalen = nodo.getTareasQueSalen();
		if (tareasQueSalen.isEmpty()) {
			nodoFinal = nodo;
			return;
		}
		for (Tarea tarea : tareasQueSalen) {
			Double margenAbsoluto = nodo.getFechaTemprana() + tarea.getDuracion();
			Nodo nodoDestino = tarea.getNodoDestino();
			if (nodoDestino.getFechaTemprana() != null &&
				margenAbsoluto < nodoDestino.getFechaTemprana()) continue;
			nodoDestino.setFechaTemprana(margenAbsoluto);
			calcularFechasTempranas(nodoDestino);
		}
	}

	public void calcularMargenes() {
		for (Tarea tarea : tareas.values()) {
			tarea.calcularMargenes();
		}
	}

	public void calcularIntervalosDeFlotamiento() {
		for (Nodo nodo : nodos) {
			nodo.calcularIntervaloDeFlotamiento();
		}
	}
}
