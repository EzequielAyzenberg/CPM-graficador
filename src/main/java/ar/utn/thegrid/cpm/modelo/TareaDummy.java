package ar.utn.thegrid.cpm.modelo;

/**
 * Tarea sin datos relevantes. Se usa para armar bien la red.
 *
 * @author eayzenberg
 */
public class TareaDummy extends Tarea{
	public TareaDummy() {
		super("", 0.0, "");
	}

	@Override
	public boolean esDummy() {
		return true;
	}

	/**
	 * Toma a una tarea y la "corta en 2"
	 * haciendo que la primera mitad sea la
	 * tarea original y la segunda mitad sea
	 * la nueva tarea dummy, actualizando las referencias
	 * de los nodos.
	 *
	 * @param original La tarea original
	 */
	public void extenderTarea(Tarea original) {
		Nodo nodoDestinoOriginal = original.getNodoDestino();
		Nodo nodoIntermedio = new Nodo();
		original.setNodoDestino(nodoIntermedio);
		this.setNodoOrigen(nodoIntermedio);
		this.setNodoDestino(nodoDestinoOriginal);
		this.setPrecedencias(original.getId()+"");
		int nroTareasDummy = original.getNroTareasDummy();
		nroTareasDummy++;
		this.setId(original.getId()+"."+nroTareasDummy);
		original.setNroTareasDummy(nroTareasDummy);
		setPrecedencias(original.getId());
	}

	@Override
	public Double getDuracion() {
		return 0.0;
	}
}
