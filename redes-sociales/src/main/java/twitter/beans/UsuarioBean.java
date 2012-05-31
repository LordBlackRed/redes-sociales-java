package twitter.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;

import org.hibernate.validator.constraints.NotEmpty;

import twitter.constantes.Constantes;
import twitter.modelo.Follows;
import twitter.modelo.Mensajes;
import twitter.modelo.MensajesPrivados;
import twitter.modelo.Usuarios;

public class UsuarioBean implements Serializable {

	private static final long serialVersionUID = 6929755619571412773L;
	private String nombre;
	@NotEmpty
	private String nick;
	private String pass;
	private String ubicacion;
	private String web;
	private String urlImagen;
	private String biografia;
	private List<Mensajes> mensajeses = new ArrayList<Mensajes>();

	private List<Mensajes> mensajesTwitter = new ArrayList<Mensajes>();
	private List<MensajesPrivados> mensajesPrivadosesForUsuarioReceptor = new ArrayList<MensajesPrivados>();
	private List<Integer> numPaginas = new ArrayList<Integer>();
	private List<Integer> numPaginasMisTwits = new ArrayList<Integer>();
	private List<Usuarios> followsesForSeguidos = new ArrayList<Usuarios>();
	private List<Usuarios> followsesForSigo = new ArrayList<Usuarios>();

	private Usuarios usuarioPrueba;

	private int numMisTwits;

	private final int twitsPorPagina = 10;
	private int paginaActual;

	private boolean login;
	private boolean unaPagina;

	FacesMessage msg;

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getNick() {
		return nick;
	}

	public void setNick(String nick) {
		this.nick = nick;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getUbicacion() {
		return ubicacion;
	}

	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	public String getWeb() {
		return web;
	}

	public void setWeb(String web) {
		this.web = web;
	}

	public String getUrlImagen() {
		return urlImagen;
	}

	public void setUrlImagen(String urlImagen) {
		this.urlImagen = urlImagen;
	}

	public String getBiografia() {
		return biografia;
	}

	public void setBiografia(String biografia) {
		this.biografia = biografia;
	}

	public UsuarioBean() {

	}

	public String registrarUsuario() {

		Usuarios usuario = new Usuarios();
		usuario.setBiografia(this.biografia);
		usuario.setNick(nick);
		usuario.setNombre(nombre);
		usuario.setPass(pass);
		usuario.setUbicacion(ubicacion);
		if (this.urlImagen.equals("")) {
			usuario.setUrlImagen(Constantes.urlImagen);
		} else {
			usuario.setUrlImagen(urlImagen);
		}
		usuario.setWeb(web);

		Constantes.usuarioDao.grabarUsuario(usuario);

		return "verPrincipal";
	}

	public String registrarse() {
		return "verRegistrar";
	}

	public String loguearUsuario() {

		Usuarios usuarioLogueado = Constantes.usuarioDao.isLogueado(this.nick,
				this.pass);

		if (usuarioLogueado != null) {
			// Metemos los datos para que aparezcan en la ventana principal
			this.ubicacion = usuarioLogueado.getUbicacion();
			this.biografia = usuarioLogueado.getBiografia();
			this.nick = usuarioLogueado.getNick();
			this.nombre = usuarioLogueado.getNombre();
			this.pass = usuarioLogueado.getPass();
			this.urlImagen = usuarioLogueado.getUrlImagen();
			this.web = usuarioLogueado.getWeb();
			this.login = true;
			Constantes.usuario = usuarioLogueado;
			this.setMensajesTwitter(Constantes.usuarioDao.obtenerMensajes(
					Constantes.usuario, 1, this.getTwitsPorPagina()));
			this.setMensajeses(Constantes.usuarioDao
					.obtenerListaMensajesMisTwitsPaginados(
							this.getMensajeses(), 1, this.getTwitsPorPagina()));
			return "loginCorrecto";
		} else {
			this.login = false;
			message("Login Incorrecto");
			return "loginIncorrecto";
		}
	}

	public boolean isLogin() {
		return login;
	}

	public void setLogin(boolean login) {
		this.login = login;
	}

	public int getTwitsPorPagina() {
		return twitsPorPagina;
	}

	public List<Mensajes> getMensajeses() {
		this.mensajeses = Constantes.usuarioDao.obtenerMisTwitsPaginados(
				Constantes.usuario, this.paginaActual, this.twitsPorPagina);
		Collections.sort(this.mensajeses);
		return mensajeses;
	}

	public void setMensajeses(List<Mensajes> mensajeses) {
		this.mensajeses = mensajeses;
	}

	public String agregarFollow() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map params = facesContext.getExternalContext().getRequestParameterMap();
		String idUsuario = (String) params.get("seguirA");

		Usuarios usuarioSeguido = Constantes.usuarioDao
				.obtenerUsuario(idUsuario);
		Usuarios usuarioSigo = Constantes.usuarioDao
				.obtenerUsuario(Constantes.usuario);
		// Buscamos primero si existe dicho follow
		boolean existe = Constantes.followDao.isExisteFollow(usuarioSeguido,
				usuarioSigo);

		if (!existe) {
			Follows follow = new Follows(usuarioSeguido, usuarioSigo);

			Constantes.usuarioDao.grabarFollow(follow);
		}

		return "followCorrecto";
	}

	public String unfollow() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map params = facesContext.getExternalContext().getRequestParameterMap();
		String idUsuario = (String) params.get("unfollowA");

		Usuarios usuarioSeguido = Constantes.usuarioDao
				.obtenerUsuario(idUsuario);
		Usuarios usuarioSigo = Constantes.usuarioDao
				.obtenerUsuario(Constantes.usuario);

		Constantes.usuarioDao.unfollow(usuarioSigo, usuarioSeguido);
		return "followCorrecto";
	}

	public String sigo() {
		return "verListaSigo";
	}

	public String seguidores() {
		return "verListaSeguidores";
	}

	public String principal() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map params = facesContext.getExternalContext().getRequestParameterMap();
		String pag = (String) params.get("irA");

		this.setPaginaActual(1);

		try {
			this.setPaginaActual(Integer.parseInt(pag));
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		this.mensajesTwitter = Constantes.usuarioDao
				.obtenerMensajes(Constantes.usuario, this.getPaginaActual(),
						this.twitsPorPagina);

		return "verPrincipal";
	}

	public String misTwits() {

		try {
			FacesContext facesContext = FacesContext.getCurrentInstance();
			Map params = facesContext.getExternalContext()
					.getRequestParameterMap();
			String pag = (String) params.get("irAMT");
			setPaginaActual(Integer.parseInt(pag));

		} catch (NumberFormatException ex) {
			setPaginaActual(1);
		}
		this.mensajeses = Constantes.usuarioDao
				.obtenerListaMensajesMisTwitsPaginados(this.mensajeses,
						getPaginaActual(), this.twitsPorPagina);

		return "verMisTwits";
	}

	public int getPaginaActual() {
		return paginaActual;
	}

	public void setPaginaActual(int paginaActual) {
		this.paginaActual = paginaActual;
	}

	public List<Mensajes> obtenerTimeLine() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map params = facesContext.getExternalContext().getRequestParameterMap();
		int pag = Integer.parseInt((String) params.get("irA"));

		return Constantes.usuarioDao.obtenerMensajes(Constantes.usuario, pag,
				this.twitsPorPagina);

	}

	public String menciones() {
		return "verMenciones";
	}

	public String descubre() {
		return "verDescubre";
	}

	public List<Mensajes> obtenerMenciones() {
		List<Mensajes> menciones = Constantes.usuarioDao
				.obtenerListaMenciones(Constantes.usuario);

		return menciones;
	}

	public List<String> obtenerTrendingTopic() {
		return Constantes.usuarioDao.obtenerTrendingTopic();
	}

	public String destruirSesion() {
		HttpSession session = (HttpSession) FacesContext.getCurrentInstance()
				.getExternalContext().getSession(true);
		session.invalidate();

		return "verIndex";
	}

	public String obtenerMensajesHashtag() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		Map params = facesContext.getExternalContext().getRequestParameterMap();
		String twitHashtag = (String) params.get("twitsHashtag");

		this.mensajesTwitter = Constantes.usuarioDao
				.obtenerListaMensajesHashtag(twitHashtag);

		Constantes.numPaginasTimeLine = 1;
		return "verPrincipal";
	}

	public List<Mensajes> getMensajesTwitter() {
		if (this.mensajesTwitter != null) {
			Collections.sort(this.mensajesTwitter);
		}
		return this.mensajesTwitter;
	}

	public void setMensajesTwitter(List<Mensajes> mensajesTwitter) {
		this.mensajesTwitter = mensajesTwitter;
	}

	public String verMensajesPrivados() {
		this.setMensajesPrivadosesForUsuarioReceptor(Constantes.usuarioDao
				.obtenerListaMensajesPrivados(Constantes.usuario));

		return "verMensajesPrivados";
	}

	public int getNumPaginasTimeLine() {
		if (Constantes.numPaginasTimeLine == 1) {
			this.unaPagina = true;
		} else {
			this.unaPagina = false;
		}

		return Constantes.numPaginasTimeLine;
	}

	public void setNumPaginasTimeLine(int numPaginas) {
		this.setNumPaginasTimeLine(numPaginas);
	}

	public List<Integer> getNumPaginas() {
		numPaginas.clear();

		if (Constantes.numPaginasTimeLine == 1) {
			this.unaPagina = true;
		} else {
			this.unaPagina = false;
		}
		for (int i = 1; i <= Constantes.numPaginasTimeLine; i++) {
			this.numPaginas.add(i);
		}
		return this.numPaginas;
	}

	public void setNumPaginas(List<Integer> numPaginas) {
		this.numPaginas = numPaginas;
	}

	public List<Integer> getNumPaginasMisTwits() {
		int numPaginasMT = Constantes.usuarioDao.obtenerNumPaginasMisTwits(
				Constantes.usuario, twitsPorPagina);

		if (numPaginasMT == 1) {
			this.unaPagina = true;
		} else {
			this.unaPagina = false;
		}

		this.numPaginasMisTwits.clear();
		for (int i = 1; i <= numPaginasMT; i++) {
			numPaginasMisTwits.add(i);
		}
		return numPaginasMisTwits;
	}

	public void setNumPaginasMisTwits(List<Integer> numPaginasMisTwits) {
		this.numPaginasMisTwits = numPaginasMisTwits;
	}

	public int getNumMisTwits() {
		this.numMisTwits = Constantes.usuarioDao
				.obtenerNumTwits(Constantes.usuario);
		return numMisTwits;
	}

	public void setNumMisTwits(int numMisTwits) {
		this.numMisTwits = numMisTwits;
	}

	public void modificarPerfil() {
		Constantes.usuarioDao.modificarPerfil(Constantes.usuario);
	}

	public List<MensajesPrivados> getMensajesPrivadosesForUsuarioReceptor() {
		return mensajesPrivadosesForUsuarioReceptor;
	}

	public void setMensajesPrivadosesForUsuarioReceptor(
			List<MensajesPrivados> mensajesPrivadosesForUsuarioReceptor) {
		this.mensajesPrivadosesForUsuarioReceptor = mensajesPrivadosesForUsuarioReceptor;
	}

	public List<Usuarios> getFollowsesForSeguidos() {
		this.followsesForSeguidos = Constantes.usuarioDao
				.obtenerListaSeguidores(Constantes.usuario);
		return this.followsesForSeguidos;
	}

	public void setFollowsesForSigo(List<Usuarios> followsesForSigo) {
		this.followsesForSigo = followsesForSigo;
	}

	public void setFollowsesForSeguidos(List<Usuarios> followsesForSeguidos) {
		this.followsesForSeguidos = followsesForSeguidos;
	}

	public List<Usuarios> getFollowsesForSigo() {

		// Lista de mis seguidores
		this.followsesForSigo = Constantes.usuarioDao
				.obtenerListaSigo(Constantes.usuario);
		return this.followsesForSigo;
	}

	public String message(String msg) {
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage message = new FacesMessage(msg);
		message.setSeverity(FacesMessage.SEVERITY_ERROR);
		context.addMessage(FacesMessage.FACES_MESSAGES, message);
		return null;
	}

	public String verUsuarios() {
		return "verListaUsuarios";
	}

	public boolean isUnaPagina() {
		return unaPagina;
	}

	public void setUnaPagina(boolean unaPagina) {
		this.unaPagina = unaPagina;
	}

	public Usuarios getUsuarioPrueba() {
		// Por si el usuario no sabe ningun login para entrar tiene este como
		// ejemplo
		this.usuarioPrueba = Constantes.usuarioDao.obtenerUsuario("1");
		this.nick = usuarioPrueba.getNick();
		this.pass = usuarioPrueba.getPass();
		return usuarioPrueba;
	}

	public void setUsuarioPrueba(Usuarios usuarioPrueba) {
		this.usuarioPrueba = usuarioPrueba;
	}

}
