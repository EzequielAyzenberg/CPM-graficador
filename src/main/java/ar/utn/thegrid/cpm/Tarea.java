/**
 * 
 */
package ar.utn.thegrid.cpm;

/**
 * Abstraccion de una Tarea
 * 
 * @author eayzenberg
 *
 */
public class Tarea {
	private Integer nro;
	private Double duracion;
	private String precedencias;
	
	public Tarea(Integer nro, Double duracion, String precedencias) {
		this.setNro(nro);
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

	public Integer getNro() {
		return nro;
	}

	public void setNro(Integer nro) {
		this.nro = nro;
	}
}
