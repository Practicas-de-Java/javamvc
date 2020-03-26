package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the tiporeserva database table.
 * 
 */
@Entity
@Table(name="tiporeserva")
@NamedQuery(name="Tiporeserva.findAll", query="SELECT t FROM Tiporeserva t")
public class Tiporeserva implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pktipo;

	@Column(nullable=false)
	private Boolean seleccionable;

	@Column(nullable=false, length=256)
	private String chartipo;

	@Column(nullable=false)
	private Long inttipo;

	//bi-directional many-to-one association to Reserva
	@OneToMany(mappedBy="tiporeserva", fetch=FetchType.EAGER)
	private List<Reserva> reservas;

	public Tiporeserva() {
	}

	public Long getPktipo() {
		return this.pktipo;
	}

	public void setPktipo(Long pktipo) {
		this.pktipo = pktipo;
	}

	public String getChartipo() {
		return this.chartipo;
	}

	public void setChartipo(String chartipo) {
		this.chartipo = chartipo;
	}

	public Long getInttipo() {
		return this.inttipo;
	}

	public void setInttipo(Long inttipo) {
		this.inttipo = inttipo;
	}

	public List<Reserva> getReservas() {
		return this.reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Reserva addReserva(Reserva reserva) {
		getReservas().add(reserva);
		reserva.setTiporeserva(this);

		return reserva;
	}

	public Reserva removeReserva(Reserva reserva) {
		getReservas().remove(reserva);
		reserva.setTiporeserva(null);

		return reserva;
	}

	public Boolean getSeleccionable() {
		return seleccionable;
	}

	public void setSeleccionable(Boolean seleccionable) {
		this.seleccionable = seleccionable;
	}

}