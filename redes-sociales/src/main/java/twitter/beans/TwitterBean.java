package twitter.beans;

import java.io.Serializable;
import java.util.List;

import twitter.constantes.Constantes;
import twitter.modelo.Mensajes;
import twitter.modelo.Usuarios;

public class TwitterBean implements Serializable{

	private static final long serialVersionUID = -4880790429598218556L;
	private List<Usuarios> usuarios;
	private List<Mensajes> mensajes;

	public TwitterBean(){
		
	}
	
	public TwitterBean(List<Usuarios> usuarios) {
		super();
		this.usuarios = usuarios;
	}

	public List<Usuarios> getUsuarios() {
		this.usuarios = Constantes.usuarioDao.obtenerUsuarios();
		return usuarios;
	}

	public void setUsuarios(List<Usuarios> usuarios) {
		this.usuarios = usuarios;
	}

	public List<Mensajes> getMensajes() {
		return mensajes;
	}

	public void setMensajes(List<Mensajes> mensajes) {
		this.mensajes = mensajes;
	}
	
}
