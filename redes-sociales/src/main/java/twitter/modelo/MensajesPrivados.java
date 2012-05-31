package twitter.modelo;
// Generated 28-may-2012 18:28:12 by Hibernate Tools 3.4.0.CR1


import java.util.Date;

/**
 * @author Rafael de los Santos Guirado
 *
 */
public class MensajesPrivados  implements java.io.Serializable {

	private static final long serialVersionUID = -8224412719744572726L;
	private Integer id;
     private Usuarios usuariosByUsuarioEmisor;
     private Usuarios usuariosByUsuarioReceptor;
     private String mensaje;
     private Date fecha;

    public MensajesPrivados() {
    }

    public MensajesPrivados(Usuarios usuariosByUsuarioEmisor, Usuarios usuariosByUsuarioReceptor, String mensaje, Date fecha) {
       this.usuariosByUsuarioEmisor = usuariosByUsuarioEmisor;
       this.usuariosByUsuarioReceptor = usuariosByUsuarioReceptor;
       this.mensaje = mensaje;
       this.fecha = fecha;
    }
   
    public Integer getId() {
        return this.id;
    }
    
    public void setId(Integer id) {
        this.id = id;
    }
    public Usuarios getUsuariosByUsuarioEmisor() {
        return this.usuariosByUsuarioEmisor;
    }
    
    public void setUsuariosByUsuarioEmisor(Usuarios usuariosByUsuarioEmisor) {
        this.usuariosByUsuarioEmisor = usuariosByUsuarioEmisor;
    }
    public Usuarios getUsuariosByUsuarioReceptor() {
        return this.usuariosByUsuarioReceptor;
    }
    
    public void setUsuariosByUsuarioReceptor(Usuarios usuariosByUsuarioReceptor) {
        this.usuariosByUsuarioReceptor = usuariosByUsuarioReceptor;
    }
    public String getMensaje() {
        return this.mensaje;
    }
    
    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
    public Date getFecha() {
        return this.fecha;
    }
    
    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }




}


