package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the reserva database table.
 * 
 */
@Entity
@Table(name="reserva")
@NamedQueries(value = { @NamedQuery(name="Reserva.findAll", query="SELECT r FROM Reserva r ORDER BY r.titulo"),
						@NamedQuery(name="Reserva.orderByPonente", query="SELECT r FROM Reserva r ORDER BY r.ponente"),
						@NamedQuery(name="Reserva.orderByFecha", query="SELECT r FROM Reserva r ORDER BY r.fechahora DESC"),
						@NamedQuery(name="Reserva.orderBySala", query="SELECT r FROM Reserva r, Sala s WHERE r.sala.nombre = s.nombre ORDER BY s.nombre"),
						@NamedQuery(name="Reserva.orderByTipo", query="SELECT r FROM Reserva r, Tiporeserva t WHERE r.tiporeserva.chartipo = t.chartipo ORDER BY t.chartipo"),
						@NamedQuery(name="Reserva.orderByUsuario", query="SELECT r FROM Reserva r, Usuario u WHERE r.usuario.nombre = u.nombre ORDER BY u.nombre")
						})
	

public class Reserva implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pkreserva;

	private Boolean cancelada;

	@Column(length=512)
	private String comentariocancelacion;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(nullable=false)
	private Date fechahora;

	private Boolean hora17;

	private Boolean hora19;

	private Boolean hora1930;

	@Column(nullable=false, length=256)
	private String ponente;

	@Column(nullable=false, length=256)
	private String titulo;

	//bi-directional many-to-one association to Sala
	@ManyToOne
	@JoinColumn(name="pksala", nullable=false)
	private Sala sala;

	//bi-directional many-to-one association to Tiporeserva
	@ManyToOne
	@JoinColumn(name="pktipo", nullable=false)
	private Tiporeserva tiporeserva;

	//bi-directional many-to-one association to Usuario
	@ManyToOne
	@JoinColumn(name="pkusuario", nullable=false)
	private Usuario usuario;

	//bi-directional many-to-one association to Prospecto
	@OneToOne(mappedBy="reserva", fetch=FetchType.EAGER)
	private Prospecto prospectos;

	public Reserva() {
	}

	public Long getPkreserva() {
		return this.pkreserva;
	}

	public void setPkreserva(Long pkreserva) {
		this.pkreserva = pkreserva;
	}

	public Boolean getCancelada() {
		return this.cancelada;
	}

	public void setCancelada(Boolean cancelada) {
		this.cancelada = cancelada;
	}

	public String getComentariocancelacion() {
		return this.comentariocancelacion;
	}

	public void setComentariocancelacion(String comentariocancelacion) {
		this.comentariocancelacion = comentariocancelacion;
	}

	public Date getFechahora() {
		return this.fechahora;
	}

	public void setFechahora(Date fechahora) {
		this.fechahora = fechahora;
	}

	public Boolean getHora17() {
		return this.hora17;
	}

	public void setHora17(Boolean hora17) {
		this.hora17 = hora17;
	}

	public Boolean getHora19() {
		return this.hora19;
	}

	public void setHora19(Boolean hora19) {
		this.hora19 = hora19;
	}

	public Boolean getHora1930() {
		return this.hora1930;
	}

	public void setHora1930(Boolean hora1930) {
		this.hora1930 = hora1930;
	}

	public String getPonente() {
		return this.ponente;
	}

	public void setPonente(String ponente) {
		this.ponente = ponente;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Sala getSala() {
		return this.sala;
	}

	public void setSala(Sala sala) {
		this.sala = sala;
	}

	public Tiporeserva getTiporeserva() {
		return this.tiporeserva;
	}

	public void setTiporeserva(Tiporeserva tiporeserva) {
		this.tiporeserva = tiporeserva;
	}

	public Usuario getUsuario() {
		return this.usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Prospecto getProspectos() {
		return this.prospectos;
	}

	public void setProspectos(Prospecto prospectos) {
		this.prospectos = prospectos;
	}
}