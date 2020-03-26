/*
 * Copyright 2009-2014 PrimeTek.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.primefaces.showcase.view.data.dataexporter;

import java.io.IOException;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.ValueChangeEvent;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.primefaces.event.RowEditEvent;
import org.primefaces.showcase.report.ElementoBoletin;
import org.primefaces.showcase.report.JasperReportGeneric;
import org.primefaces.showcase.reserva.Perfil;
import org.primefaces.showcase.reserva.Prospecto;
import org.primefaces.showcase.reserva.Reserva;
import org.primefaces.showcase.reserva.Sala;
import org.primefaces.showcase.reserva.Tiporeserva;
import org.primefaces.showcase.reserva.Usuario;
import org.primefaces.showcase.service.EntityDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.sun.mail.smtp.SMTPTransport;

@Component
@ManagedBean(name="dataExporterView")
@ApplicationScoped
public class DataExporterView implements Serializable, Converter {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 3985109352477447113L;
	
	@Autowired(required=true)
	EntityDao<Perfil> perfilImpl;
	@Autowired(required=true)
	EntityDao<Reserva> reservaImpl;
	@Autowired(required=true)
	EntityDao<Sala> salaImpl;
	@Autowired(required=true)
	EntityDao<Tiporeserva> tipoReservaImpl;
	@Autowired(required=true)
	EntityDao<Usuario> usuarioImpl;
	@Autowired(required=true)
	EntityDao<Prospecto> prospectoImpl;
	
    private String nombre = "";
    private String contrasenya = "";
    private List<Perfil> perfiles = null;
    private List<Reserva> reservas = null;
    private List<Reserva> reservasUsuarioConectado = null;
    private List<Reserva> reservasPorFechaAsc = null;
    private List<Reserva> reservasPorFechaAscOrdenanza = null;
    private List<Sala> salas = null;
    private List<Tiporeserva> tiposReserva = null;
    private List<Sala> salasUsuario = null;
    private List<Tiporeserva> tiposReservaUsuario = null;
    private List<Usuario> usuarios = null;
    private String mensaje = "";
    private Prospecto currentProspecto;
    private Perfil newPerfil = new Perfil();
    private Sala newSala = new Sala();
    private Usuario newUsuario = new Usuario();
    private Tiporeserva newTipoReserva = new Tiporeserva();
    private Reserva newReserva = new Reserva();
    private Usuario usuarioConectado;
    private String antiguaContrasenya = "";
    private String nuevaContrasenya = "";
    private String nuevaContrasenya2 = "";
    private Usuario usuarioEditado = new Usuario();
    private Perfil currentPerfil = new Perfil();
    private Sala currentSala = new Sala();
    private Tiporeserva currentTipoReserva = new Tiporeserva();
    private Usuario currentUsuario = new Usuario();
    
    private String nombreSala = "";
    private String tipoReservaParaEsteUsuario = "";
    private String usuarioAsignado = "";

	private int statusReserva = 1;
	private Date minDate;
	private Date maxDate;
	private Date maxActualizacionDate;

	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaDesde;
	@Temporal(TemporalType.TIMESTAMP)
	private Date fechaHasta;
	
	private String mensajesalUsuario = "";

	private String currentProspectoDate;

	private Reserva currentReserva;
	
    private static final String SMTP_SERVER = "smtp server ";
    private static final String USERNAME = "";
    private static final String PASSWORD = "";

    private static final String EMAIL_FROM = "From@gmail.com";
    private static final String EMAIL_TO_CC = "";

    private static final String EMAIL_SUBJECT = "Cancelación de reserva de sala";

    public DataExporterView() {
    	SpringBeanAutowiringSupport.processInjectionBasedOnCurrentContext(this);
    }
    
    public void volver(ActionEvent event) {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if(this.usuarioConectado != null) {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
	    	else if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");
	    	} else
	    		ec.redirect(ec.getRequestContextPath());
	    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void volverAdministracion(ActionEvent event) {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administracion.xhtml");
	    	else
	    		ec.redirect(ec.getRequestContextPath());
	    		
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void volverAdministrarReservas(ActionEvent event) {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void eventosPlanificados() {
    	if( this.usuarioConectado != null)
    		this.usuarioConectado.getPerfil().setCharperfil("");
    	
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(ec.getRequestContextPath() + "/views/ordenanzas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public void cambiarContrasenya() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    	String contrasenyaActual = this.usuarioConectado.getContrasenya();
    	if( !(contrasenyaActual.equalsIgnoreCase(this.antiguaContrasenya) && this.nuevaContrasenya.equalsIgnoreCase(this.nuevaContrasenya2))) {
    	    try {
    	    	this.mensajesalUsuario += "No autorizado o nueva contraseña no coincide en los dos valores introducidos";
    			ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");
    			return;
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	} else {
    		this.usuarioConectado.setContrasenya(nuevaContrasenya);
    		usuarioImpl.setEntityClass(Usuario.class);
    		try {
				usuarioImpl.merge(usuarioConectado);
    			ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}  		
    	}
    }
    
    public void registrarse() {
    	boolean accept = false;
    	Usuario solicitante = null;
    	try {
    		this.mensajesalUsuario = "";
    		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
    		LocalDateTime now = LocalDateTime.now(); 
    		
    		now = now.plusMonths(1);
    		now = now.minusDays(now.getDayOfMonth() - 1); 
    		this.maxActualizacionDate  = Date.from( now.atZone( ZoneId.systemDefault()).toInstant());
    		
    		now = now.plusMonths(1);
    		now = now.minusDays(now.getDayOfMonth() - 1); 
    		this.minDate  = Date.from( now.atZone( ZoneId.systemDefault()).toInstant());

    		now = now.plusMonths(2);
    		now = now.minusDays(1); 
    		this.maxDate  = Date.from( now.atZone( ZoneId.systemDefault()).toInstant());

    		usuarioImpl.setEntityClass(Usuario.class);
			List<Usuario> l = this.usuarioImpl.findAll();
        	perfilImpl.setEntityClass(Perfil.class);
			List<Perfil> p = this.perfilImpl.findAll();
        	salaImpl.setEntityClass(Sala.class);
			List<Sala>  s = this.salaImpl.findAll();
        	tipoReservaImpl.setEntityClass(Tiporeserva.class);
			List<Tiporeserva> r = this.tipoReservaImpl.findAll();
			
			currentSala = s.get(0);
			currentTipoReserva = r.get(0);
			currentUsuario = l.get(0);
			
			this.newReserva.setSala(s.get(0));
			this.newReserva.setTiporeserva(r.get(0));
			this.newReserva.setUsuario(l.get(0));

			for( Perfil pf : p) {
				if(pf.getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO")) {
					newUsuario.setPerfil(pf);
					usuarioEditado.setPerfil(pf);
					currentPerfil = pf;
				}
			}
					
			for( Usuario u : l) {
				if( u.getNombre().equalsIgnoreCase(nombre) && u.getContrasenya().equalsIgnoreCase(contrasenya)){
					accept = true;
					solicitante = u;
					usuarioConectado = u;
					reservaImpl.setEntityClass(Reserva.class);
					String nombre = u.getNombre();
					reservasUsuarioConectado = reservaImpl.findAllByParametro("SELECT r FROM Reserva r, Usuario u WHERE r.usuario.nombre = u.nombre AND r.usuario.nombre=:nombre ORDER BY r.fechahora DESC", nombre);
					if(reservasUsuarioConectado == null )
						reservasUsuarioConectado.get(0).setUsuario(usuarioConectado);
					break;
				}
			}
			if(accept) {
			    try {
		    		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			    	// DISTINGUIR USUARIO ADMINISTRADOR O NO : DIRIGIR A LA PAGINA ADECUADA
			    	if(solicitante.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR")) //Administrador
			    		ec.redirect(ec.getRequestContextPath() + "/views/administracion.xhtml");
			    	else if(solicitante.getPerfil().getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO"))
			    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");// Usuario normal
			    	else {
						this.mensajesalUsuario += "No autorizado. Póngase en contacto con los responsables del sistema";
			    		ec.redirect(ec.getRequestContextPath() + "/views/login.xhtml");					    		
			    	}
				} catch (IOException e) {
					accept = false;
					mensaje = e.getMessage();
				}
			} else {
	    		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
				this.mensajesalUsuario += "No autorizado. Póngase en contacto con los responsables del sistema";
	    		ec.redirect(ec.getRequestContextPath() + "/views/login.xhtml");		
			}
		} catch (Exception e1) {
			accept = false;
			mensaje=e1.getMessage();
		}
    	if(!accept) this.mensajesalUsuario += mensaje +"";
    }
    public void administrarPerfilUsuario() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "/views/administrarPerfilUsuario.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }
    public void cambiarSala(ValueChangeEvent vcEvent) {
    	this.mensajesalUsuario = "";
    	Long ide = (Long)vcEvent.getComponent().getAttributes().get("ide");
    	String sala = (String)vcEvent.getComponent().getAttributes().get("sala");
		reservaImpl.setEntityClass(Reserva.class);
			Reserva r;
			try {
				r = reservaImpl.findById(ide);
				for( Sala s : salas) {
					if( s.getNombre().equalsIgnoreCase(sala)) {
						try {
							r.setSala(s);
							reservaImpl.merge(r);
							reservas = reservaImpl.findAll();break;
						} catch (Exception e) {
							this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION DE LA SALA";
						}
					}
				}
			} catch (Exception e1) {
				this.mensajesalUsuario +="FALLÓ LA ACTUALIZACION DE LA SALA";
			}
    }
    public void cambiarTipoReserva(ValueChangeEvent vcEvent) {
    	this.mensajesalUsuario = "";
    	Long ide = (Long)vcEvent.getComponent().getAttributes().get("ide");
    	String tiporeserva = (String)vcEvent.getComponent().getAttributes().get("tiporeserva");
		reservaImpl.setEntityClass(Reserva.class);
		Reserva r;
		try {
			r = reservaImpl.findById(ide);
			for( Tiporeserva t : this.tiposReserva) {
				if( t.getChartipo().equalsIgnoreCase(tiporeserva)) {
					try {
						r.setTiporeserva(t);
						reservaImpl.merge(r);
						reservas = reservaImpl.findAll();break;
					} catch (Exception e) {
						this.mensajesalUsuario +="FALLÓ LA ACTUALIZACION DEL TIPO DE RESERVA";
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    public void cambiarUsuario(ValueChangeEvent vcEvent) {
    	this.mensajesalUsuario = "";
    	Long ide = (Long)vcEvent.getComponent().getAttributes().get("ide1");
    	String nombreusuario = (String)vcEvent.getComponent().getAttributes().get("nombreusuario");
		reservaImpl.setEntityClass(Reserva.class);
		Reserva r;
		try {
			r = reservaImpl.findById(ide);
			for( Usuario u : this.usuarios) {
				if( u.getNombre().equalsIgnoreCase(nombreusuario)) {
					try {
						r.setUsuario(u);
						reservaImpl.merge(r);
						reservas = reservaImpl.findAll();break;
					} catch (Exception e) {
						this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION DEL USUARIO";
					}
				}
			}
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    }
    	
    public void salvarPerfilEditado() {
    	
    }
  
    public void usuarioEdit(RowEditEvent<Usuario> event) {
    	Usuario u = event.getObject();
    }
     
    public void usuarioEditCancel(RowEditEvent<Usuario> event) {
    	Usuario u = event.getObject();
    }

    public void cambiarPerfil(Long pk) {
    	this.mensajesalUsuario = "";
    	Usuario uu = null;
    	for( Usuario u : usuarios) {
    		if(pk == u.getPkusuario()) {
    			if( u.getPerfil().getIntperfil() == 0 ) u.getPerfil().setIntperfil(1);
    			else if(pk == u.getPkusuario()) u.getPerfil().setIntperfil(0);
    			uu = u;
    			break;
    		}
    	}
		this.usuarioImpl.setEntityClass(Usuario.class);
		for (Perfil p : perfiles) {
			if (p.getIntperfil() == uu.getPerfil().getIntperfil()) {
				uu.setPerfil(p);
				break;
			}
		}
		for( int i = 0; i < usuarios.size(); i++) {
			if(this.usuarios.get(i).getPkusuario() == uu.getPkusuario()) {
				this.usuarios.get(i).setPerfil(uu.getPerfil());
				try {
					this.usuarioImpl.merge(this.usuarios.get(i));
				} catch (Exception e) {
					this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION. ALGUNOS USUARIOS SE HAN QUEDADO CON EL VALOR ANTIGUO";
				}
				break;
			}
		}
    }

    public void salvarUsuarioEditado() {
    	this.mensajesalUsuario = "";
		this.usuarioImpl.setEntityClass(Usuario.class);
		for(Usuario u : this.usuarios) {
					try {
						this.usuarioImpl.merge(u);
					} catch (Exception e) {
						this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION. ALGUNOS USUARIOS SE HAN QUEDADO CON EL VALOR ANTIGUO";
					}
				}				

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarUsuarios.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A PAGINA. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
   	
    }
    public void salvarSalaEditada() {
    	this.mensajesalUsuario = "";
		this.salaImpl.setEntityClass(Sala.class);
		for(Sala s : this.salas) {
					try {
						this.salaImpl.merge(s);
					} catch (Exception e) {
						this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION. ALGUNAS SALAS QUEDARON CON SUS VALORES PREVIOS";
					}
				}				

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarSalas.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A PAGINA. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}   	
    }
    public void salvarTipoEditado() {
    	this.mensajesalUsuario = "";
		this.tipoReservaImpl.setEntityClass(Tiporeserva.class);
		for(Tiporeserva t : this.tiposReserva) {
					try {
						this.tipoReservaImpl.merge(t);
					} catch (Exception e) {
						this.mensajesalUsuario += "FALLÓ LA ACTUALIZACION. ALGUNOS TIPOS QUEDARON CON SUS VALORES PREVIOS";
					}
				}				

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarTipoReserva.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A PAGINA. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
   	
    }

    public void salvarTipoReservaEditado() {
    	
    }
    
	private List<Reserva> saveChange2(Long ide) {
		this.mensajesalUsuario = "";
    	for(int i = 0; i < reservas.size(); i++) {
    		if( reservas.get(i).getPkreserva() == Long.valueOf(ide)){
    			try {
					reservaImpl.merge(reservas.get(i));
					break;
				} catch (Exception e) {
					this.mensajesalUsuario += "FALLÓ LA RESERVA DE SALA EDITADA. SE HA QUEDADO CON EL VALOR ANTIGUO";
				}
    		}
    	}
		return reservas;
	}

    public void borrarPerfil() {
    	
    }
    public void borrarSala() {
    	
    }
    public void borrarUsuario(ActionEvent event) {
    	this.mensajesalUsuario = "";
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		usuarioImpl.setEntityClass(Usuario.class);
		for(Usuario u : usuarios) {
			if(u.getPkusuario().intValue() == ide.intValue()) {
				try {
					usuarioImpl.remove(ide);
				} catch (Exception e1) {
					e1.printStackTrace();
					this.mensajesalUsuario += "FALLÓ EL BORRADO DEL USUARIO. NO SE HA REALIZADO";
				}				
			}
		}

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarUsuarios.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ LA RESERVA DE SALA EDITADA. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
   	
    }
    public void borrarTipoReserva() {
    	
    }
	public void salvarReservaEditada(ActionEvent event) {
		String sala = "";
		String tipoReserva = "";
		String usuario = "";
		List<UIComponent> l = event.getComponent().getParent().getParent().getChildren();// COLUMNAS
		for(UIComponent u : l) {
			List<UIComponent> ll = u.getChildren(); // HIJOS DE LAS COLUMNAS
			for(UIComponent uu : ll) {
				if(uu.getAttributes().containsValue("sala")) {
					sala = (String) uu.getAttributes().get("value");
				}
				if(uu.getAttributes().containsValue("tipoReserva")) {
					tipoReserva = (String) uu.getAttributes().get("value");
				}
				if(uu.getAttributes().containsValue("usuario")) {
					usuario = (String) uu.getAttributes().get("value");
				}
			}
			
		}	
		
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		this.reservaImpl.setEntityClass(Reserva.class);
		try {
			Reserva r = this.reservaImpl.findById(ide);
			for(Sala s : salas) {
				if(s.getNombre().equalsIgnoreCase(sala)) {
					r.setSala(s);
					break;
				}
			}
			for(Tiporeserva t : this.tiposReserva) {
				if(t.getChartipo().equalsIgnoreCase(tipoReserva)) {
					r.setTiporeserva(t);
					break;
				}
			}
			for(Usuario u : this.usuarios) {
				if(u.getNombre().equalsIgnoreCase(usuario)) {
					r.setUsuario(u);
					break;
				}
			}
			reservaImpl.merge(r);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void cambiaSala(final AjaxBehaviorEvent event)  {
		String value = (String) event.getComponent().getAttributes().get("value");
	}
	public void capturaSala(final AjaxBehaviorEvent event)  {
		Sala value = (Sala) event.getComponent().getAttributes().get("value");
	}	
    public void borrarReserva(ActionEvent event) {
    	this.mensajesalUsuario = "";
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		this.reservaImpl.setEntityClass(Reserva.class);
		for(Reserva r : this.reservas) {
			if(r.getPkreserva().intValue() == ide.intValue()) {
				try {
					this.prospectoImpl.setEntityClass(Prospecto.class);
					Prospecto p = r.getProspectos();
					if(p != null)
						prospectoImpl.remove(p.getPkprospecto());
					this.reservaImpl.setEntityClass(Reserva.class);
					reservaImpl.remove(ide);
				} catch (Exception e1) {
					e1.printStackTrace();
					this.mensajesalUsuario += "FALLÓ EL BORRADO DEL RESERVA. NO SE HA REALIZADO";
				}				
			}
		}

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
		} catch (IOException e) {
			this.mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE RESERVAS.";
		}
    }

    public void borrarReservaUsuario(ActionEvent event) {
    	this.mensajesalUsuario = "";
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		this.reservaImpl.setEntityClass(Reserva.class);
		for(Reserva r : this.reservasUsuarioConectado) {
			if(r.getPkreserva().intValue() == ide.intValue()) {
				try {
					this.prospectoImpl.setEntityClass(Prospecto.class);
					Prospecto p = r.getProspectos();
					if( p != null)
						prospectoImpl.remove(p.getPkprospecto());
					this.reservaImpl.setEntityClass(Reserva.class);
					reservaImpl.remove(ide);
				} catch (Exception e1) {
					e1.printStackTrace();
					this.mensajesalUsuario += "FALLÓ EL BORRADO DEL RESERVA. NO SE HA REALIZADO";
				}				
			}
		}

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
		} catch (IOException e) {
			this.mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE RESERVAS.";
		}
    }

    public void editaProspectoOrdenanza(ActionEvent event) {
    	this.mensajesalUsuario = "";
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		this.reservaImpl.setEntityClass(Reserva.class);
		try {
			this.currentReserva = this.reservaImpl.findById(ide);
	    	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	    	currentProspectoDate = df.format(currentReserva.getFechahora());
			this.currentProspecto = this.currentReserva.getProspectos();
			this.currentProspecto.setSala(currentReserva.getSala().getNombre());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(ec.getRequestContextPath() + "/views/ordenanzasProspecto.xhtml");
		} catch (IOException e) {
			this.mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE RESERVAS.";
		}
    }

    
    public void editaProspecto(ActionEvent event) {
    	this.mensajesalUsuario = "";
		String id = event.getComponent().getAttributes().get("ide").toString();
		Long ide = Long.valueOf(id);
		this.reservaImpl.setEntityClass(Reserva.class);
		try {
			this.currentReserva = this.reservaImpl.findById(ide);
	    	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	    	currentProspectoDate = df.format(currentReserva.getFechahora());
			this.currentProspecto = this.currentReserva.getProspectos();
			this.currentProspecto.setSala(currentReserva.getSala().getNombre());
		} catch (Exception e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}

	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(ec.getRequestContextPath() + "/views/prospecto.xhtml");
		} catch (IOException e) {
			this.mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE RESERVAS.";
		}
    }

    public void nuevaSala() {
		try {
			newSala.setPksala(null);
			salaImpl.setEntityClass(Sala.class);
			salaImpl.persist(newSala);
			newSala.setPksala(null);
			salas = salaImpl.findAll();
		} catch (Exception e1) {
			e1.printStackTrace();
			mensajesalUsuario += "FALLÓ EL ALTA DE SALA. NO SE HA REALIZADO";
		}
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarSalas.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE USUARIOS. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
    	
    }

    public void nuevoTiporeserva() {
		try {
			newTipoReserva.setPktipo(null);
			tipoReservaImpl.setEntityClass(Tiporeserva.class);
			tipoReservaImpl.persist(newTipoReserva);
			newTipoReserva.setPktipo(null);
			this.tiposReserva = tipoReservaImpl.findAll();
		} catch (Exception e1) {
			e1.printStackTrace();
			mensajesalUsuario += "FALLÓ EL ALTA DE TIPO. NO SE HA REALIZADO";
		}
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarTipoReserva.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE USUARIOS. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
    	
    }

    public void nuevoUsuario() {
		try {
			newUsuario.setPkusuario(null);
			Integer intPerfil = currentPerfil.getIntperfil();
        	perfilImpl.setEntityClass(Perfil.class);
			List<Perfil> p = this.perfilImpl.findAll();

			for( Perfil pf : p) {
				if(pf.getIntperfil() == intPerfil) {
					currentPerfil = pf;
				}
			}
			newUsuario.setPerfil(currentPerfil);
			usuarioImpl.setEntityClass(Usuario.class);
			usuarioImpl.persist(newUsuario);
			newUsuario.setPkusuario(null);
			
		} catch (Exception e1) {
			e1.printStackTrace();
			mensajesalUsuario += "FALLÓ EL ALTA DEL NUEVO USUARIO. NO SE HA REALIZADO";
		}
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
	    		ec.redirect(ec.getRequestContextPath() + "/views/administrarUsuarios.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE USUARIOS. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
	}

    public void guardarProspecto() {
    	
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
			ec.redirect(ec.getRequestContextPath() + "/views/prospecto.xhtml");
		} catch (IOException e) {
			mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA ADMINISTRACION DE USUARIOS. SE HA QUEDADO CON EL VALOR ANTIGUO";
		}
    }

    public void actualizaProspecto() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    	mensajesalUsuario = "";
    	prospectoImpl.setEntityClass(Prospecto.class);
    	try {
			prospectoImpl.merge(currentProspecto);
			String nombrePerfilUsuario = this.usuarioConectado.getPerfil().getCharperfil();
			if(nombrePerfilUsuario.equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
			else
				if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO"))
					ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");
				
		} catch (Exception e) {
			mensajesalUsuario += "FALLÓ EL ALTA DE PROSPECTO. NO SE HA GRABADO";
		}
    }


    public void exportarAWord() {
    	JasperReportGeneric report = new JasperReportGeneric();
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("TITULO", this.currentProspecto.getTitulo());
		parameters.put("CONVOCANTES", this.currentProspecto.getEntidades());
		parameters.put("PONENTES", this.currentProspecto.getPonentes());
		parameters.put("PRESIDE", this.currentProspecto.getPreside());
		parameters.put("MODERA", this.currentProspecto.getModera());
		parameters.put("TIPOEVENTO", this.currentProspecto.getTipoevento());
		parameters.put("SALA", this.currentProspecto.getSala());
		parameters.put("FECHA", this.currentProspectoDate);
		parameters.put("HORA", this.currentProspecto.getFechahora());
		parameters.put("RESUMEN", this.currentProspecto.getResumenevento());
		parameters.put("CONTACTO", this.currentProspecto.getDatoscontacto());
		
		report.generaInforme(request, response, parameters, this.currentProspecto.getTitulo());
    }

    public void generarBoletin() {    	
    	prospectoImpl.setEntityClass(Prospecto.class);
    	JasperReportGeneric report = new JasperReportGeneric();
    	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<ElementoBoletin> boletos = new ArrayList<ElementoBoletin>();
		List<Prospecto> prospectos = new ArrayList<Prospecto>();
		String query = "SELECT p FROM Prospecto p, Reserva r WHERE r.fechahora>=:fechaDesde AND r.fechahora<=:fechaHasta AND r.pkreserva=p.reserva.pkreserva ORDER BY r.fechahora ASC";
		try {
			prospectos = prospectoImpl.findAllByDate(query, fechaDesde, fechaHasta);
			boletos = getBoletos(prospectos);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		parameters.put("Boletos", boletos);
		
		report.generaBoletin(request, response, parameters, "Boletín válido desde " + fechaDesde.toString() + " hasta " + fechaHasta.toString(), boletos);
    }

    private List<ElementoBoletin> getBoletos(List<Prospecto> prospectos) {
    	List<ElementoBoletin> boletos = new ArrayList<ElementoBoletin>();
    	for(Prospecto p: prospectos) {
    		ElementoBoletin currentElementoBoletin = new ElementoBoletin();
    		
    		String diaSemana = getDiaSemana(p.getReserva().getFechahora());
    		currentElementoBoletin.setFecha(diaSemana.toUpperCase());
    		
    		currentElementoBoletin.setHora(" " + p.getFechahora() + " ");
    		if( !p.getEntidades().isEmpty())
    			currentElementoBoletin.setSecciones(p.getEntidades().toUpperCase());
    		else
    			currentElementoBoletin.setSecciones("");
    			
    		if(!p.getIntervinientes().isEmpty())
    			currentElementoBoletin.setIntervinientes(" Interviene(n): " + p.getIntervinientes() + " ");
    		else
    			currentElementoBoletin.setIntervinientes("");
    		
    		if( !p.getModera().isEmpty())
    			currentElementoBoletin.setModera(" Modera: " + p.getModera() + " ");
    		else
    			currentElementoBoletin.setModera("");

    		if(!p.getPonentes().isEmpty())
    			currentElementoBoletin.setPonentes(" Ponente(s): " + p.getPonentes() + " ");
    		else
    			currentElementoBoletin.setPonentes("");
    		
    		if(!p.getPresentador().isEmpty())
    			currentElementoBoletin.setPresentador(" Presenta: " + p.getPresentador() + " ");
    		else
    			currentElementoBoletin.setPresentador("");
    		
    		if(!p.getPreside().isEmpty())
    			currentElementoBoletin.setPreside(" Preside o Introduce: " + p.getPreside() + " ");
    		else
    			currentElementoBoletin.setPreside("");
    			
    		currentElementoBoletin.setSala("SALA " + p.getReserva().getSala().getNombre() + " ");
    		currentElementoBoletin.setSecciones(p.getEntidades());
    		currentElementoBoletin.setTitulo(p.getTitulo());
    		boletos.add(currentElementoBoletin);
    	}
    	return boletos;
    	
	}

	private String getDiaSemana(Date fechahora) {
		String fecha = new SimpleDateFormat("EEEE dd").format(fechahora);
		return fecha;
	}

	public void nuevoProspecto() {
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
    	mensajesalUsuario = "";
    	prospectoImpl.setEntityClass(Prospecto.class);
    	try {
			prospectoImpl.merge(currentProspecto);
			if(this.usuarioConectado != null) {
			if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
			else if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO")) {
				ec.redirect(ec.getRequestContextPath() + "/views/administrarReservasPorUsuario.xhtml");
			} else {
				ec.redirect(ec.getRequestContextPath() + "/views/ordenanzas.xhtml");								
			}
			} else {
				ec.redirect(ec.getRequestContextPath() + "/views/ordenanzas.xhtml");				
			}
		} catch (Exception e) {
			e.printStackTrace();
			mensajesalUsuario += "FALLÓ EL ALTA DE PROSPECTO. NO SE HA GRABADO";
		}
    }
    
    public void nuevaReserva() {
    	boolean persistir = false;
		try {
			this.mensajesalUsuario = "";
			newReserva.setPkreserva(null);
			this.mensajesalUsuario = "";
			String sala = currentSala.getNombre();
        	this.salaImpl.setEntityClass(Sala.class);
			List<Sala> s = this.salaImpl.findAll();

			for( Sala sl : s) {
				if(sl.getNombre().equalsIgnoreCase(sala)) {
					currentSala = sl;break;
				}
			}

			String tipoReserva = currentTipoReserva.getChartipo();
        	this.tipoReservaImpl.setEntityClass(Tiporeserva.class);
        	if( currentTipoReserva.getChartipo() == null || currentTipoReserva.getChartipo().isEmpty()) {
        		currentTipoReserva.setChartipo("HORARIA");        		
        	}
        	
			List<Tiporeserva> t = this.tipoReservaImpl.findAll();

			for( Tiporeserva tr : t) {
				if(tr.getChartipo().equalsIgnoreCase(tipoReserva)) {
					currentTipoReserva = tr;break;
				}
			}

			//if( currentUsuario.getNombre() == null )
				currentUsuario = this.usuarioConectado;
			
			String usuario = currentUsuario.getNombre();
        	this.usuarioImpl.setEntityClass(Usuario.class);
			List<Usuario> u = this.usuarioImpl.findAll();
			for( Usuario us : u) {
				if(us.getNombre().equalsIgnoreCase(usuario)) {
					currentUsuario = us;break;
				}
			}

			newReserva.setSala(currentSala);
			newReserva.setTiporeserva(currentTipoReserva);
			newReserva.setUsuario(currentUsuario);
			reservaImpl.setEntityClass(Reserva.class);
			
			if( usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR")) {
				persistir = procesaSubplantacion(newReserva);
			} else { // Usuario cualificado
				persistir = validaReserva(newReserva);
			}
			
			if( persistir )
				reservaImpl.persist(newReserva);
			else
				this.mensajesalUsuario += "FALLÓ EL ALTA DE NUEVA RESERVA. NO SE HA REALIZADO POR COINCIDIR CON OTRA RESERVA PREVIA";
			
			reservasUsuarioConectado = reservaImpl.findAllByParametro("SELECT r FROM Reserva r, Usuario u WHERE r.usuario.nombre = u.nombre AND r.usuario.nombre=:nombre ORDER BY r.fechahora DESC", nombre);
			if(reservasUsuarioConectado == null )
				reservasUsuarioConectado.get(0).setUsuario(usuarioConectado);
			
		} catch (Exception e1) {
			e1.printStackTrace();
			this.mensajesalUsuario += "FALLÓ EL ALTA DE NUEVA RESERVA. NO SE HA REALIZADO";
			return;
		}
	    ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	    try {
	    	this.mensajesalUsuario += "TODO FUE BIEN";
	    	currentProspecto = new Prospecto();
	    	currentProspecto.setSala(newReserva.getSala().getNombre());
	    	DateFormat df = DateFormat.getDateInstance(DateFormat.LONG, Locale.getDefault());
	    	currentProspectoDate = df.format(newReserva.getFechahora());
	    	currentProspecto.setReserva(newReserva);
			ec.redirect(ec.getRequestContextPath() + "/views/prospecto.xhtml");
		} catch (IOException e) {
			this.mensajesalUsuario += "FALLÓ EL REDIRECCIONAMIENTO A LA CREACION DEL PROSPECTO";
		}
	    //this.mensajesalUsuario = "";
	}

	private boolean validaReserva(Reserva newReserva2) {
		if( newReserva2.getSala().getNombre().equalsIgnoreCase("SALON DE ACTOS"))
			return false;
    	reservaImpl.setEntityClass(Reserva.class);
    	boolean cancelada = newReserva2.getCancelada();
    	Date fecha = newReserva2.getFechahora();
    	boolean hora17 = newReserva2.getHora17();
    	boolean hora19 = newReserva2.getHora19();
    	boolean hora1930 = newReserva2.getHora1930();
    	String sala = newReserva2.getSala().getNombre();
    	String tipoReserva = newReserva2.getTiporeserva().getChartipo();
    	List<Reserva> todas = null;
		try {
			todas = reservaImpl.findAll();
			for(Reserva r : todas ) {
		    	reservaImpl.setEntityClass(Reserva.class);
		    	String salaR = r.getSala().getNombre();
		    	Date fechaR = r.getFechahora();
		    	boolean hora17R = r.getHora17();
		    	boolean hora19R = r.getHora19();
		    	boolean hora1930R = r.getHora1930();
		    	boolean canceladaR = r.getCancelada();
		    	String tipoReservaR = r.getTiporeserva().getChartipo();
		    	if( salaR.equalsIgnoreCase(sala)) {
		    		if(fechaR.getTime() == fecha.getTime()) {
	    				if(!(cancelada && canceladaR)) {
	    						if( (hora17 && hora17R) || (hora19 && hora19R) || (hora1930 && hora1930R) || (hora19 && hora1930R) || (hora1930 && hora19R)) {
		    						return false;
		    				}	
		    			}
		    		}
		    	} 		
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

    private boolean procesaSubplantacion(Reserva newReserva2) {
    	reservaImpl.setEntityClass(Reserva.class);
    	boolean cancelada = newReserva2.getCancelada();
    	Date fecha = newReserva2.getFechahora();
    	boolean hora17 = newReserva2.getHora17();
    	boolean hora19 = newReserva2.getHora19();
    	boolean hora1930 = newReserva2.getHora1930();
    	String sala = newReserva2.getSala().getNombre();
    	String tipoReserva = newReserva2.getTiporeserva().getChartipo();
    	List<Reserva> todas = null;
		try {
			todas = reservaImpl.findAll();
			for(Reserva r : todas ) {
		    	reservaImpl.setEntityClass(Reserva.class);
		    	String salaR = r.getSala().getNombre();
		    	Date fechaR = r.getFechahora();
		    	boolean hora17R = r.getHora17();
		    	boolean hora19R = r.getHora19();
		    	boolean hora1930R = r.getHora1930();
		    	boolean canceladaR = r.getCancelada();
		    	String tipoReservaR = r.getTiporeserva().getChartipo();
		    	if( salaR.equalsIgnoreCase(sala)) {
		    		if(fechaR.getTime() == fecha.getTime()) {
	    				if(!(cancelada && canceladaR)) {
    						if( (hora17 && hora17R) || (hora19 && hora19R) || (hora1930 && hora1930R) || (hora19 && hora1930R) || (hora1930 && hora19R)) {
    							if( tipoReservaR.equalsIgnoreCase("HORARIO TIPICO") && (tipoReserva.equalsIgnoreCase("ACTO DE LA JUNTA DE GOBIERNO NO CANCELABLE") && !tipoReserva.equalsIgnoreCase("ALQUILER"))) {
		    						this.enviaMail(r.getUsuario().getMail(), r);
		    						reservaImpl.remove(r.getPkreserva());
		    						return true;
		    					} else if(!tipoReserva.equalsIgnoreCase("ACTO DE LA JUNTA DE GOBIERNO NO CANCELABLE") && tipoReservaR.equalsIgnoreCase("ACTO DE LA JUNTA DE GOBIERNO NO CANCELABLE")) {
		    						return false;
		    					}
		    				} 
		    			}
		    		}
		    	} 		
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return true;
	}

	private void enviaMail(String mail, Reserva r) {
		
        Properties prop = System.getProperties();
        prop.put("mail.smtp.host", SMTP_SERVER); //optional, defined in SMTPTransport
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "25"); // default port 25

        Session session = Session.getInstance(prop, null);
        Message msg = new MimeMessage(session);

        try {
		
			// from
            msg.setFrom(new InternetAddress(EMAIL_FROM));

			// to 
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(mail, false));

			// cc
            msg.setRecipients(Message.RecipientType.CC,
                    InternetAddress.parse(EMAIL_TO_CC, false));

			// subject
            msg.setSubject(EMAIL_SUBJECT);
			
			// content 
            msg.setText("Cancelada su reserva de sala " + r.getSala().getNombre() + " de fecha " + r.getFechahora().toString() + "por acto corporativo prioritario del Ateneo");
			
            msg.setSentDate(new Date());

			// Get SMTPTransport
            SMTPTransport t = (SMTPTransport) session.getTransport("smtp");
			
			// connect
            t.connect(SMTP_SERVER, USERNAME, PASSWORD);
			
			// send
            t.sendMessage(msg, msg.getAllRecipients());

            System.out.println("Response: " + t.getLastServerResponse());

            t.close();

        } catch (MessagingException e) {
            e.printStackTrace();
        }


	   }  		

    public void nuevoPerfil() {
    	
    }
	private List<Reserva> saveChange(List<Reserva> reservas, Reserva r) {
		try {
			reservaImpl.persist(r);
		} catch (Exception e) {
			mensajesalUsuario += "FALLÓ LA RESERVA NUEVA. NO SE HA RESERVADO";
		}
		reservas.add(r);
		return reservas;
	}   
    public void administrarSalas() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "/views/administrarSalas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void administrarTipoReserva() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			ec.redirect(ec.getRequestContextPath() + "/views/administrarTipoReserva.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void administrarUsuario() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			this.perfilImpl.setEntityClass(Perfil.class);
			List<Perfil> p = this.perfilImpl.findAll();
			for( Perfil pf : p) {
				if(pf.getCharperfil().equalsIgnoreCase("USUARIO CUALIFICADO")) {
					newUsuario.setPerfil(pf);
					break;
				}
			}
			if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/administrarUsuarios.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void pedirBoletin() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/pedirBoletin.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }

    public void ordenarTítulos() {
			statusReserva = 1;
    }
    public void ordenarPonentes() {
			statusReserva = 2;
    }
    public void ordenarFechas() {
			statusReserva = 3;
    }
    public void ordenarSalas() {
			statusReserva = 4;
    }
    public void ordenarTipos() {
			statusReserva = 5;
    }
    public void ordenarUsuarios() {
			statusReserva = 6;
    }

    public void administrarReservaEvento() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    public void salir() {
    		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
			try {
				ec.redirect(ec.getRequestContextPath());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
    }

    public void volver() {
		ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
		try {
			if( this.usuarioConectado.getPerfil().getCharperfil().equalsIgnoreCase("ADMINISTRADOR"))
				ec.redirect(ec.getRequestContextPath() + "/views/administrarReservas.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

    public EntityDao<Perfil> getPerfilImpl() {
		return perfilImpl;
	}
	public void setPerfilImpl(EntityDao<Perfil> perfilImpl) {
		this.perfilImpl = perfilImpl;
	}
	public EntityDao<Reserva> getReservaImpl() {
		return reservaImpl;
	}
	public void setReservaImpl(EntityDao<Reserva> reservaImpl) {
		this.reservaImpl = reservaImpl;
	}
	public EntityDao<Sala> getSalaImpl() {
		return salaImpl;
	}
	public void setSalaImpl(EntityDao<Sala> salaImpl) {
		this.salaImpl = salaImpl;
	}
	public EntityDao<Tiporeserva> getTipoReservaImpl() {
		return tipoReservaImpl;
	}
	public void setTipoReservaImpl(EntityDao<Tiporeserva> tipoReservaImpl) {
		this.tipoReservaImpl = tipoReservaImpl;
	}
	public EntityDao<Usuario> getUsuarioImpl() {
		return usuarioImpl;
	}
	public void setUsuarioImpl(EntityDao<Usuario> usuarioImpl) {
		this.usuarioImpl = usuarioImpl;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getContrasenya() {
		return contrasenya;
	}
	public void setContrasenya(String contrasenya) {
		this.contrasenya = contrasenya;
	}
	public List<Perfil> getPerfiles() {
		try {
			perfilImpl.setEntityClass(Perfil.class);
			perfiles = perfilImpl.findAllOrderBy("Perfil.findAll");
		} catch (Exception e) {
			mensajesalUsuario += "PERFILES NO CARGADOS";
		}
		return perfiles;
	}
	public void setPerfiles(List<Perfil> perfiles) {
		this.perfiles = perfiles;
	}
	public List<Reserva> getReservas() {
		reservaImpl.setEntityClass(Reserva.class);
		try {
			switch(this.statusReserva) {
			case 1:	reservas = reservaImpl.findAllOrderBy("Reserva.findAll");			break;
			case 2: reservas = reservaImpl.findAllOrderBy("Reserva.orderByPonente");	break;
			case 3: reservas = reservaImpl.findAllOrderBy("Reserva.orderByFecha");		break;
			case 4: reservas = reservaImpl.findAllOrderBy("Reserva.orderBySala");		break;
			case 5: reservas = reservaImpl.findAllOrderBy("Reserva.orderByTipo");		break;
			case 6: reservas = reservaImpl.findAllOrderBy("Reserva.orderByUsuario");	break;
			}
		} catch (Exception e) {
			mensajesalUsuario += "RESERVAS DE SALAS NO CARGADAS";
		}
		return reservas;
	}

	private List<Reserva> getOrderByUsuario(List<Reserva> reservas2) {
		return null;
	}

	private List<Reserva> getOrderByTipo(List<Reserva> reservas2) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<Reserva> getOrderBySala(List<Reserva> reservas2) {
		// TODO Auto-generated method stub
		return null;
	}

	public void setReservas(List<Reserva> reservas) {
		this.reservas = reservas;
	}
	public List<Sala> getSalas() {
		try {
			salaImpl.setEntityClass(Sala.class);
			salas = salaImpl.findAllOrderBy( "Sala.findAll");
		} catch (Exception e) {
			mensajesalUsuario += "RELACION DE SALAS NO CARGADA";
		}
		return salas;
	}
	public void setSalas(List<Sala> salas) {
		this.salas = salas;
	}
	public List<Tiporeserva> getTiposReserva() {
		try {
			tipoReservaImpl.setEntityClass(Tiporeserva.class);
			tiposReserva = tipoReservaImpl.findAllOrderBy("Tiporeserva.findAll");
		} catch (Exception e) {
			mensajesalUsuario += "TIPOS DE RESERVAS NO CARGADAS";
		}
		return tiposReserva;
	}
	public void setTiposReserva(List<Tiporeserva> tiposReserva) {
		this.tiposReserva = tiposReserva;
	}
	public List<Usuario> getUsuarios() {
		try {
			usuarioImpl.setEntityClass(Usuario.class);
			usuarios = usuarioImpl.findAllOrderBy("Usuario.findAll");
		} catch (Exception e) {
			mensajesalUsuario += "USUARIOS NO CARGADOS";
		}			
		return usuarios;
	}
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public Perfil getNewPerfil() {
		return newPerfil;
	}

	public void setNewPerfil(Perfil newPerfil) {
		this.newPerfil = newPerfil;
	}

	public Sala getNewSala() {
		return newSala;
	}

	public void setNewSala(Sala newSala) {
		this.newSala = newSala;
	}

	public Usuario getNewUsuario() {
		return newUsuario;
	}

	public void setNewUsuario(Usuario newUsuario) {
		this.newUsuario = newUsuario;
	}

	public Tiporeserva getNewTipoReserva() {
		return newTipoReserva;
	}

	public void setNewTipoReserva(Tiporeserva newTipoReserva) {
		this.newTipoReserva = newTipoReserva;
	}

	public Reserva getNewReserva() {
		return newReserva;
	}

	public void setNewReserva(Reserva newReserva) {
		this.newReserva = newReserva;
	}

	public Usuario getUsuarioConectado() {
		return usuarioConectado;
	}

	public void setUsuarioConectado(Usuario usuarioConectado) {
		this.usuarioConectado = usuarioConectado;
	}

	public Usuario getUsuarioEditado() {
		return usuarioEditado;
	}

	public void setUsuarioEditado(Usuario usuarioEditado) {
		this.usuarioEditado = usuarioEditado;
	}

	public Perfil getCurrentPerfil() {
		return currentPerfil;
	}

	public void setCurrentPerfil(Perfil currentPerfil) {
		this.currentPerfil = currentPerfil;
	}

	public Sala getCurrentSala() {
		return currentSala;
	}

	public void setCurrentSala(Sala currentSala) {
		this.currentSala = currentSala;
	}

	public Tiporeserva getCurrentTipoReserva() {
		return currentTipoReserva;
	}

	public void setCurrentTipoReserva(Tiporeserva currentTipoReserva) {
		this.currentTipoReserva = currentTipoReserva;
	}

	public Usuario getCurrentUsuario() {
		return currentUsuario;
	}

	public void setCurrentUsuario(Usuario currentUsuario) {
		this.currentUsuario = currentUsuario;
	}

	public String getNombreSala() {
		return nombreSala;
	}

	public void setNombreSala(String nombreSala) {
		this.nombreSala = nombreSala;
	}


	public String getTipoReservaParaEsteUsuario() {
		return tipoReservaParaEsteUsuario;
	}

	public void setTipoReservaParaEsteUsuario(String tipoReservaParaEsteUsuario) {
		this.tipoReservaParaEsteUsuario = tipoReservaParaEsteUsuario;
	}

	public String getUsuarioAsignado() {
		return usuarioAsignado;
	}

	public void setUsuarioAsignado(String usuarioAsignado) {
		this.usuarioAsignado = usuarioAsignado;
	}

	@Override
	public Object getAsObject(FacesContext context, UIComponent component, String value) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Object value) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getStatusReserva() {
		return statusReserva;
	}

	public void setStatusReserva(int statusReserva) {
		this.statusReserva = statusReserva;
	}

	public List<Reserva> getReservasUsuarioConectado() {
		return reservasUsuarioConectado;
	}

	public void setReservasUsuarioConectado(List<Reserva> reservasUsuarioConectado) {
		this.reservasUsuarioConectado = reservasUsuarioConectado;
	}

	public List<Reserva> getReservasPorFechaAsc() {
		reservaImpl.setEntityClass(Reserva.class);
		try {
			reservasPorFechaAsc = reservaImpl.findAllOrderBy("Reserva.orderByFecha");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return reservasPorFechaAsc;
	}

	public void setReservasPorFechaAsc(List<Reserva> reservasPorFechaAsc) {
		this.reservasPorFechaAsc = reservasPorFechaAsc;
	}

	public Date getMinDate() {
		return minDate;
	}

	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	public Date getMaxDate() {
		return maxDate;
	}

	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	public String getMensajesalUsuario() {
		return mensajesalUsuario;
	}

	public void setMensajesalUsuario(String mensajesalUsuario) {
		this.mensajesalUsuario = mensajesalUsuario;
	}

	public String getNuevaContrasenya() {
		return nuevaContrasenya;
	}

	public void setNuevaContrasenya(String nuevaContrasenya) {
		this.nuevaContrasenya = nuevaContrasenya;
	}

	public String getNuevaContrasenya2() {
		return nuevaContrasenya2;
	}

	public void setNuevaContrasenya2(String nuevaContrasenya2) {
		this.nuevaContrasenya2 = nuevaContrasenya2;
	}

	public String getAntiguaContrasenya() {
		return antiguaContrasenya;
	}

	public void setAntiguaContrasenya(String antiguaContrasenya) {
		this.antiguaContrasenya = antiguaContrasenya;
	}

	public Prospecto getCurrentProspecto() {
		return currentProspecto;
	}

	public void setCurrentProspecto(Prospecto currentProspecto) {
		this.currentProspecto = currentProspecto;
	}

	public String getCurrentProspectoDate() {
		return currentProspectoDate;
	}

	public void setCurrentProspectoDate(String currentProspectoDate) {
		this.currentProspectoDate = currentProspectoDate;
	}

	public List<Sala> getSalasUsuario() {
		salaImpl.setEntityClass(Sala.class);
		try {
			salasUsuario = salaImpl.findAllByBooleano("SELECT s FROM Sala s WHERE s.reservable =:nombre", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return salasUsuario;
	}

	public void setSalasUsuario(List<Sala> salasUsuario) {
		this.salasUsuario = salasUsuario;
	}

	public List<Tiporeserva> getTiposReservaUsuario() {
		tipoReservaImpl.setEntityClass(Tiporeserva.class);
		try {
			tiposReservaUsuario = tipoReservaImpl.findAllByBooleano("SELECT t FROM Tiporeserva t WHERE t.seleccionable =:nombre", true);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return tiposReservaUsuario;
	}

	public void setTiposReservaUsuario(List<Tiporeserva> tiposReservaUsuario) {
		this.tiposReservaUsuario = tiposReservaUsuario;
	}

	public Date getMaxActualizacionDate() {
		return maxActualizacionDate;
	}

	public void setMaxActualizacionDate(Date maxActualizacionDate) {
		this.maxActualizacionDate = maxActualizacionDate;
	}

	public List<Reserva> getReservasPorFechaAscOrdenanza() {
		if(reservasPorFechaAscOrdenanza == null) {
			reservasPorFechaAscOrdenanza = new ArrayList<Reserva>();
		}
		reservasPorFechaAscOrdenanza.clear();
		//DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");  
		LocalDateTime now = LocalDateTime.now(); 
		if(reservasPorFechaAsc == null)
			reservasPorFechaAsc = getReservasPorFechaAsc();
		
		for( Reserva r : this.reservasPorFechaAsc) {
			if(!r.getFechahora().before(Date.from(now.atZone( ZoneId.systemDefault()).toInstant()))) {
				reservasPorFechaAscOrdenanza.add(r);
			}
		}
		return reservasPorFechaAscOrdenanza;
	}

	public void setReservasPorFechaAscOrdenanza(List<Reserva> reservasPorFechaAscOrdenanza) {
		this.reservasPorFechaAscOrdenanza = reservasPorFechaAscOrdenanza;
	}

	public Date getFechaDesde() {
		return fechaDesde;
	}

	public void setFechaDesde(Date fechaDesde) {
		this.fechaDesde = fechaDesde;
	}

	public Date getFechaHasta() {
		return fechaHasta;
	}

	public void setFechaHasta(Date fechaHasta) {
		this.fechaHasta = fechaHasta;
	}
 }
