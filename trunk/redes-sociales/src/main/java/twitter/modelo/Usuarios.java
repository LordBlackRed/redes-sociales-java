package twitter.modelo;

// Generated 28-may-2012 18:28:12 by Hibernate Tools 3.4.0.CR1

import java.util.HashSet;
import java.util.Set;

/**
 * @author Rafael de los Santos Guirado
 * 
 */
public class Usuarios implements java.io.Serializable {

	private static final long serialVersionUID = -9002943559510864300L;

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Usuarios other = (Usuarios) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	private Integer id;
	private String nombre;
	private String pass;
	private String nick;
	private String ubicacion;
	private String web;
	private String biografia;
	private String urlImagen;
	private Set<MensajesPrivados> mensajesPrivadosesForUsuarioReceptor = new HashSet<MensajesPrivados>(
			0);
	private Set<Follows> followsesForSeguidos = new HashSet<Follows>(0);
	private Set<MensajesPrivados> mensajesPrivadosesForUsuarioEmisor = new HashSet<MensajesPrivados>(
			0);
	private Set<Follows> followsesForSigo = new HashSet<Follows>(0);
	private Set<Mensajes> mensajeses = new HashSet<Mensajes>(0);

	public Usuarios() {
	}

	public Usuarios(String nombre, String pass, String nick) {
		this.nombre = nombre;
		this.pass = pass;
		this.nick = nick;
	}

	public Usuarios(String nombre, String pass, String nick, String ubicacion,
			String web, String biografia, String urlImagen,
			Set<MensajesPrivados> mensajesPrivadosesForUsuarioReceptor,
			Set<Follows> followsesForSeguidos,
			Set<MensajesPrivados> mensajesPrivadosesForUsuarioEmisor,
			Set<Follows> followsesForSigo, Set<Mensajes> mensajeses) {
		this.nombre = nombre;
		this.pass = pass;
		this.nick = nick;
		this.ubicacion = ubicacion;
		this.web = web;
		this.biografia = biografia;
		this.urlImagen = urlImagen;
		this.mensajesPrivadosesForUsuarioReceptor = mensajesPrivadosesForUsuarioReceptor;
		this.followsesForSeguidos = followsesForSeguidos;
		this.mensajesPrivadosesForUsuarioEmisor = mensajesPrivadosesForUsuarioEmisor;
		this.followsesForSigo = followsesForSigo;
		this.mensajeses = mensajeses;
	}

	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getNombre() {
		return this.nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPass() {
		return this.pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getNick() {
		return this.nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getUbicacion() {
		return this.ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getWeb() {
		return this.web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getBiografia() {
		return this.biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public String getUrlImagen() {
		return this.urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public Set<MensajesPrivados> getMensajesPrivadosesForUsuarioReceptor() {
		return this.mensajesPrivadosesForUsuarioReceptor;
	}

	public void setMensajesPrivadosesForUsuarioReceptor(
			Set<MensajesPrivados> mensajesPrivadosesForUsuarioReceptor) {
		this.mensajesPrivadosesForUsuarioReceptor = mensajesPrivadosesForUsuarioReceptor;
	}

	public Set<Follows> getFollowsesForSeguidos() {
		return this.followsesForSeguidos;
	}

	public void setFollowsesForSeguidos(Set<Follows> followsesForSeguidos) {
		this.followsesForSeguidos = followsesForSeguidos;
	}

	public Set<MensajesPrivados> getMensajesPrivadosesForUsuarioEmisor() {
		return this.mensajesPrivadosesForUsuarioEmisor;
	}

	public void setMensajesPrivadosesForUsuarioEmisor(
			Set<MensajesPrivados> mensajesPrivadosesForUsuarioEmisor) {
		this.mensajesPrivadosesForUsuarioEmisor = mensajesPrivadosesForUsuarioEmisor;
	}

	public Set<Follows> getFollowsesForSigo() {
		return this.followsesForSigo;
	}

	public void setFollowsesForSigo(Set<Follows> followsesForSigo) {
		this.followsesForSigo = followsesForSigo;
	}

	public Set<Mensajes> getMensajeses() {
		return this.mensajeses;
	}

	public void setMensajeses(Set<Mensajes> mensajeses) {
		this.mensajeses = mensajeses;
	}

}
