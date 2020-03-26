package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the usuario database table.
 * 
 */
@Entity
@Table(name="usuario")
@NamedQuery(name="Usuario.findAll", query="SELECT u FROM Usuario u")
public class Usuario implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pkusuario;

	@Column(nullable=false, length=9)
	private String contrasenya;

	@Column(nullable=false, length=128)
	private String mail;

	@Column(nullable=false, length=128)
	private String nombre;

	//bi-directional many-to-one association to Reserva
	@OneToMany(mappedBy="usuario", fetch=FetchType.EAGER)
	private List<Reserva> reservas;

	//bi-directional many-to-one association to Perfil
	@ManyToOne
	@JoinColumn(name="pkperfil", nullable=false)
	private Perfil perfil;

	public Usuario() {
	}

	public Long getPkusuario() {
		return this.pkusuario;
	}

	public void setPkusuario(Long pkusuario) {
		this.pkusuario = pkusuario;
	}

	public String getContrasenya() {
		return this.contrasenya;
	}

	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}

	public String getMail() {
		return this.mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public List<Reserva> getReservas() {
		return this.reservas;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}

	public Reserva addReserva(Reserva reserva) {
		getReservas().add(reserva);
		reserva.setUsuario(this);

		return reserva;
	}

	public Reserva removeReserva(Reserva reserva) {
		getReservas().remove(reserva);
		reserva.setUsuario(null);

		return reserva;
	}

	public Perfil getPerfil() {
		return this.perfil;
	}

	public void setPerfil(Perfil perfil) {
		this.perfil = perfil;
	}

}