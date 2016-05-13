package ar.utn.thegrid.cpm;

/**
 * Tarea sin datos relevantes. Se usa para armar bien la red.
 *
 * @author eayzenberg
 */
public class TareaDummy extends Tarea{
	public TareaDummy() {
		super("", 0.0, "");
	}

	/**
	 * Toma a una tarea y la "corta en 2"
	 * haciendo que la primera mitad sea la
	 * tarea original y la segunda mitad sea
	 * la nueva tarea dummy, actualizando las referencias
	 * de los nodos.<br><br>
	 *
	 *  <b>O--------------Tarea------------->O</b> <br>
	 *  pasa a ser <br>
	 *  <b>O---Tarea---->O----Dummy--->O</b> <br>
	 *
	 * @param original
	 */
	public void extenderTarea(Tarea original) {
		Nodo nodoDestinoOriginal = original.getNodoDestino();
		Nodo nodoIntermedio = new Nodo();
		original.setNodoDestino(nodoIntermedio);
		this.setNodoOrigen(nodoIntermedio);
		this.setNodoDestino(nodoDestinoOriginal);
		this.setPrecedencias(original.getId()+"");
		int nroTareasDummy = original.getNroTareasDummy();
		this.setId(original.getId()+"-"+nroTareasDummy+1);
		original.setNroTareasDummy(nroTareasDummy+1);
	}

	@Override
	public Double getDuracion() {
		return 0.0;
	}
}