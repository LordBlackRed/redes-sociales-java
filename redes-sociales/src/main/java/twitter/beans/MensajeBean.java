package twitter.beans;

import java.io.Serializable;
import java.util.Date;

import twitter.constantes.Constantes;
import twitter.modelo.Mensajes;
import twitter.modelo.MensajesPrivados;
import twitter.modelo.Usuarios;

public class MensajeBean implements Serializable{

	private static final long serialVersionUID = -1553733759874958537L;
	private String mensaje;
	private Date fecha;
	private String idUsuarioRte;

	public MensajeBean() {

	}

	public String grabarMensaje() {
		Mensajes mensaje = new Mensajes(Constantes.usuario, this.mensaje,
				this.fecha);

		boolean correcto = Constantes.mensajeDao.grabarMensaje(mensaje,
				Constantes.usuario);

		if (correcto)
			return "nuevoMensajeCorrecto";
		else
			return "nuevoMensajeFallo";
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMensaje() {
		return mensaje;
	}

	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	public String grabarMensajePrivado() {
		System.out.println("va a agrabar el mensaje privado");

		Usuarios usuarioRemitente = Constantes.usuarioDao
				.obtenerUsuario(this.idUsuarioRte);

		MensajesPrivados mensajePrivado = new MensajesPrivados(
				Constantes.usuario, usuarioRemitente, mensaje, new Date());

		Constantes.mensajeDao.grabarMensajePrivado(mensajePrivado);
		mensaje = "";
		idUsuarioRte = "";
		return "correcto";
	}

	public String getIdUsuarioRte() {
		return idUsuarioRte;
	}

	public void setIdUsuarioRte(String idUsuarioRte) {
		this.idUsuarioRte = idUsuarioRte;
	}
}
