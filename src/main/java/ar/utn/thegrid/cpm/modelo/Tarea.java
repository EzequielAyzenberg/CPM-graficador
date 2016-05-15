/**
 *
 */
package ar.utn.thegrid.cpm.modelo;

/**
 * Abstraccion de una Tarea
 *
 * @author eayzenberg
 *
 */
public class Tarea {
	private String id;
	private Double duracion = 0.0;
	private String precedencias;
	private Nodo nodoOrigen, nodoDestino;
	private int nroTareasDummy = 0;
	private double margenLibre;
	private double margenTotal;
	private boolean critica;

	public Tarea(String id, Double duracion, String precedencias) {
		this.setId(id);
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

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Nodo getNodoOrigen() {
		return nodoOrigen;
	}

	public void setNodoOrigen(Nodo nodo) {
		if (nodoOrigen != null)
			nodoOrigen.getTareasQueSalen().remove(this);
		this.nodoOrigen = nodo;
		nodo.getTareasQueSalen().add(this);
		nodo.actualizarDescripcion();
	}

	public Nodo getNodoDestino() {
		return nodoDestino;
	}

	public void setNodoDestino(Nodo nodo) {
		if (nodoDestino != null)
			nodoDestino.getTareasQueArriban().remove(this);
		this.nodoDestino = nodo;
		nodo.getTareasQueArriban().add(this);
		nodo.actualizarDescripcion();
	}

	public boolean esHoja() {
		return nodoDestino.getTareasQueArriban().size() == 1
			&& nodoDestino.getTareasQueSalen().isEmpty();
	}

	public int getNroTareasDummy() {
		return nroTareasDummy ;
	}

	public void setNroTareasDummy(int i) {
		nroTareasDummy = i;
	}

	@Override
	public String toString() {
		return "["+id+", "+duracion+", ("+precedencias+")]";
	}

	public void calcularMargenes() {
		margenLibre = nodoDestino.getFechaTemprana()
				- nodoOrigen.getFechaTemprana() - duracion;
		margenTotal = nodoDestino.getFechaTardia()
				- nodoOrigen.getFechaTemprana() - duracion;
		setCritica(margenLibre + margenTotal == 0);
	}

	public void setCritica(boolean critica) {
		this.critica = critica;
	}

	public boolean isCritica() {
		return critica;
	}

	public boolean esDummy(){
		return false;
	}
}
