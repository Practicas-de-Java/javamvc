package org.primefaces.showcase.reserva;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the perfil database table.
 * 
 */
@Entity
@Table(name="perfil")
@NamedQuery(name="Perfil.findAll", query="SELECT p FROM Perfil p")
public class Perfil implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(unique=true, nullable=false)
	private Long pkperfil;

	@Column(nullable=false, length=128)
	private String charperfil;

	@Column(nullable=false)
	private Integer intperfil;

	//bi-directional many-to-one association to Usuario
	@OneToMany(mappedBy="perfil", fetch=FetchType.EAGER)
	private List<Usuario> usuarios;

	public Perfil() {
	}

	public Long getPkperfil() {
		return this.pkperfil;
	}

	public void setPkperfil(Long pkperfil) {
		this.pkperfil = pkperfil;
	}

	public String getCharperfil() {
		return this.charperfil;
	}

	public void setCharperfil(String charperfil) {
		this.charperfil = charperfil;
	}

	public Integer getIntperfil() {
		return this.intperfil;
	}

	public void setIntperfil(Integer intperfil) {
		this.intperfil = intperfil;
	}

	public List<Usuario> getUsuarios() {
		return this.usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public Usuario addUsuario(Usuario usuario) {
		getUsuarios().add(usuario);
		usuario.setPerfil(this);

		return usuario;
	}

	public Usuario removeUsuario(Usuario usuario) {
		getUsuarios().remove(usuario);
		usuario.setPerfil(null);

		return usuario;
	}

}