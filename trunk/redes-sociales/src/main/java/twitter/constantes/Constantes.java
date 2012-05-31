package twitter.constantes;

import twitter.controlador.dao.FollowDao;
import twitter.controlador.dao.MensajeDao;
import twitter.controlador.dao.UsuarioDao;
import twitter.modelo.Usuarios;

public class Constantes {

	public static UsuarioDao usuarioDao = new UsuarioDao();
	public static FollowDao followDao = new FollowDao();
	public static int numPaginasTimeLine;
	public static Usuarios usuario;
	public static MensajeDao mensajeDao = new MensajeDao();
	public static final String urlImagen = "http://geeksp.com/wp-content/uploads/2010/09/twitter-egg-260.jpg";

}
