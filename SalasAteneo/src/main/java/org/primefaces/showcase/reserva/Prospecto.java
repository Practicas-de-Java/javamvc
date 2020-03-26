package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the prospecto database table.
 * 
 */
@Entity
@Table(name="prospecto")
@NamedQuery(name="Prospecto.findAll", query="SELECT p FROM Prospecto p")
public class Prospecto implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pkprospecto;

	@Column(nullable=false, length=2048)
	private String datosadministracion;

	@Column(nullable=false, length=1024)
	private String datoscontacto;

	@Column(nullable=false, length=1024)
	private String entidades;

	@Column(nullable=false, length=2048)
	private String equiposymedios;

	@Column(nullable=false, length=512)
	private String fechahora;

	@Column(length=1024)
	private String intervinientes;

	@Column(nullable=false, length=128)
	private String modera;

	private Long numeroasistentes;

	@Column(length=4096)
	private String otrosdatos;

	@Column(nullable=false, length=1024)
	private String ponentes;

	@Column(nullable=false, length=128)
	private String presentador;

	@Column(nullable=false, length=128)
	private String preside;

	@Column(nullable=false)
	private Boolean regalo;

	@Column(nullable=false, length=4096)
	private String resumenevento;

	@Column(nullable=false, length=128)
	private String sala;

	@Column(nullable=false, length=256)
	private String tipoevento;

	@Column(nullable=false, length=256)
	private String titulo;

	//bi-directional many-to-one association to Reserva
	@OneToOne
	@JoinColumn(name="pkreserva", nullable=false)
	private Reserva reserva;

	public Prospecto() {
	}

	public Long getPkprospecto() {
		return this.pkprospecto;
	}

	public void setPkprospecto(Long pkprospecto) {
		this.pkprospecto = pkprospecto;
	}

	public String getDatosadministracion() {
		return this.datosadministracion;
	}

	public void setDatosadministracion(String datosadministracion) {
		this.datosadministracion = datosadministracion;
	}

	public String getDatoscontacto() {
		return this.datoscontacto;
	}

	public void setDatoscontacto(String datoscontacto) {
		this.datoscontacto = datoscontacto;
	}

	public String getEntidades() {
		return this.entidades;
	}

	public void setEntidades(String entidades) {
		this.entidades = entidades;
	}

	public String getEquiposymedios() {
		return this.equiposymedios;
	}

	public void setEquiposymedios(String equiposymedios) {
		this.equiposymedios = equiposymedios;
	}

	public String getFechahora() {
		return this.fechahora;
	}

	public void setFechahora(String fechahora) {
		this.fechahora = fechahora;
	}

	public String getIntervinientes() {
		return this.intervinientes;
	}

	public void setIntervinientes(String intervinientes) {
		this.intervinientes = intervinientes;
	}

	public String getModera() {
		return this.modera;
	}

	public void setModera(String modera) {
		this.modera = modera;
	}

	public Long getNumeroasistentes() {
		return this.numeroasistentes;
	}

	public void setNumeroasistentes(Long numeroasistentes) {
		this.numeroasistentes = numeroasistentes;
	}

	public String getOtrosdatos() {
		return this.otrosdatos;
	}

	public void setOtrosdatos(String otrosdatos) {
		this.otrosdatos = otrosdatos;
	}

	public String getPonentes() {
		return this.ponentes;
	}

	public void setPonentes(String ponentes) {
		this.ponentes = ponentes;
	}

	public String getPresentador() {
		return this.presentador;
	}

	public void setPresentador(String presentador) {
		this.presentador = presentador;
	}

	public String getPreside() {
		return this.preside;
	}

	public void setPreside(String preside) {
		this.preside = preside;
	}

	public Boolean getRegalo() {
		return this.regalo;
	}

	public void setRegalo(Boolean regalo) {
		this.regalo = regalo;
	}

	public String getResumenevento() {
		return this.resumenevento;
	}

	public void setResumenevento(String resumenevento) {
		this.resumenevento = resumenevento;
	}

	public String getSala() {
		return this.sala;
	}

	public void setSala(String sala) {
		this.sala = sala;
	}

	public String getTipoevento() {
		return this.tipoevento;
	}

	public void setTipoevento(String tipoevento) {
		this.tipoevento = tipoevento;
	}

	public String getTitulo() {
		return this.titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public Reserva getReserva() {
		return this.reserva;
	}

	public void setReserva(Reserva reserva) {
		this.reserva = reserva;
	}

}
