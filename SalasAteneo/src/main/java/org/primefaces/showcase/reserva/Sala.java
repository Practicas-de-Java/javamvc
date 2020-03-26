package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the sala database table.
 * 
 */
@Entity
@Table(name="sala")
@NamedQuery(name="Sala.findAll", query="SELECT s FROM Sala s")
public class Sala implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pksala;

	@Column(nullable=false)
	private Integer aforo;

	@Column(nullable=false, length=512)
	private String nombre;

	@Column(nullable=false)
	private Boolean reservable;

	//bi-directional many-to-one association to Reserva
	@OneToMany(mappedBy="sala", fetch=FetchType.EAGER)
	private List<Reserva> reservas;

	public Sala() {
	}

	public Long getPksala() {
		return this.pksala;
	}

	public void setPksala(Long pksala) {
		this.pksala = pksala;
	}

	public Integer getAforo() {
		return this.aforo;
	}

	public void setAforo(Integer aforo) {
		this.aforo = aforo;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Boolean getReservable() {
		return this.reservable;
	}

	public void setReservable(Boolean reservable) {
		this.reservable = reservable;
	}

	public List<Reserva> getReservas() {
		return this.reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Reserva addReserva(Reserva reserva) {
		getReservas().add(reserva);
		reserva.setSala(this);

		return reserva;
	}

	public Reserva removeReserva(Reserva reserva) {
		getReservas().remove(reserva);
		reserva.setSala(null);

		return reserva;
	}

}