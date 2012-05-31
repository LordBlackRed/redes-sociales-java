package twitter.controlador.dao;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

import twitter.clases.TrendingTopic;
import twitter.constantes.Constantes;
import twitter.modelo.Follows;
import twitter.modelo.Mensajes;
import twitter.modelo.MensajesPrivados;
import twitter.modelo.Usuarios;
import twitter.util.UtilHibernate;

/**
 * @author Rafael de los Santos Guirado
 * 
 */

public class UsuarioDao {

	// public static int numPaginasTimeLine;
	//
	public Usuarios isLogueado(String nick, String pass) {

		// Buscamos si el usuario existe en la BD

		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria cr = ses.createCriteria(Usuarios.class);
		cr.add(Restrictions.eq("nick", new String(nick)));
		cr.add(Restrictions.eq("pass", new String(pass)));
		// En una restricci—n metemos 2 where (a=1 and a=2) or b=3
		// cr.add(Restrictions.and())
		Usuarios usuario = (Usuarios) cr.uniqueResult();

		return usuario;

	}

	public List<Mensajes> obtenerMensajes(Usuarios usuario, int paginaActual,
			int twitsPorPagina) {
		Session ses = UtilHibernate.getSessionFactory().openSession();

		Integer idUsuario = obtenerIdUsuario(usuario);

		Criteria crFollows = ses.createCriteria(Follows.class);
		crFollows.add(Restrictions.eq("usuariosBySigo",
				(Usuarios) ses.get(Usuarios.class, idUsuario)));
		// debe ser una lista de follows
		@SuppressWarnings("unchecked")
		List<Follows> follows = crFollows.list();
		Iterator<Follows> itFollows = follows.iterator();
		List<Usuarios> usuariosFollows = new ArrayList<Usuarios>();
		while (itFollows.hasNext()) {
			usuariosFollows.add(itFollows.next().getUsuariosBySeguidos());
		}

		// Buscamos ahora los mensajes dle usuario de la sesi—n
		Criteria crUsuarioSesion = ses.createCriteria(Mensajes.class);
		crUsuarioSesion.add(Restrictions.eq("usuarios",
				ses.get(Usuarios.class, idUsuario)));
		@SuppressWarnings("unchecked")
		List<Mensajes> mensajesUsuario = crUsuarioSesion.list();

		boolean error = false;
		if (usuariosFollows.isEmpty()) {
			error = true;
		}
		if (!error) {
			Criteria cr = ses.createCriteria(Mensajes.class);
			cr.add(Restrictions.in("usuarios", usuariosFollows));

			@SuppressWarnings("unchecked")
			List<Mensajes> mensajes = cr.list();

			for (Mensajes msg : mensajesUsuario) {
				mensajes.add(msg);
			}

			Collections.sort(mensajes);

			List<Mensajes> mensajesPaginados = new ArrayList<Mensajes>();
			paginaActual--;
			int k = paginaActual * twitsPorPagina;
			for (int i = k; i < k + twitsPorPagina; i++) {
				try {
					mensajesPaginados.add(mensajes.get(i));
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					break;
				}
			}

			BigDecimal big = new BigDecimal((double) mensajes.size()
					/ (double) twitsPorPagina);

			big = big.setScale(0, RoundingMode.UP);

			Constantes.numPaginasTimeLine = big.intValue();

			return mensajesPaginados;
		} else {
			//No tiene follows

			List<Mensajes> mensajesPaginados = new ArrayList<Mensajes>();
			paginaActual--;
			int k = paginaActual * twitsPorPagina;
			for (int i = k; i < k + twitsPorPagina; i++) {
				try {
					mensajesPaginados.add(mensajesUsuario.get(i));
				} catch (IndexOutOfBoundsException e) {
					e.printStackTrace();
					break;
				}
			}
			
			BigDecimal big = new BigDecimal((double) mensajesUsuario.size()
					/ (double) twitsPorPagina);
			
			big = big.setScale(0, RoundingMode.UP);
			
			Constantes.numPaginasTimeLine = big.intValue();
			
			return mensajesPaginados;
		}
	}

	public Integer obtenerIdUsuario(Usuarios usuario) {
		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria crUsuario = ses.createCriteria(Usuarios.class);
		crUsuario.add(Restrictions.eq("nick", usuario.getNick()));

		Integer id = ((Usuarios) crUsuario.uniqueResult()).getId();

		return id;
	}

	public Usuarios obtenerUsuario(Usuarios usuario) {
		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria crUsuario = ses.createCriteria(Usuarios.class);
		crUsuario.add(Restrictions.eq("nick", usuario.getNick()));

		Usuarios user = ((Usuarios) crUsuario.uniqueResult());
		return user;
	}

	public static boolean verUsuarios(Usuarios usuarios) {

		return false;
	}

	public List<Usuarios> obtenerUsuarios() {

		Session ses = UtilHibernate.getSessionFactory().openSession();
		// Buscmaos todos los usuarios
		Criteria cr = ses.createCriteria(Usuarios.class);

		@SuppressWarnings("unchecked")
		List<Usuarios> usuarios = cr.list();

		// Cogemos los usuarios a los que sigue
		List<Usuarios> usuariosFollow = obtenerListaSeguidores(Constantes.usuario);

		List<Usuarios> usuariosTotal = new ArrayList<Usuarios>(usuarios);
		// Comparamos y obtenemos los no repetidos
		for (Usuarios us : usuariosFollow) {
			Iterator<Usuarios> itUsuarios = usuarios.iterator();
			while (itUsuarios.hasNext()) {
				Usuarios usIt = itUsuarios.next();
				if (usIt.getId().equals(us.getId())) {
					usuariosTotal.remove(us);
				}

			}
		}

		for (Usuarios usTotal : usuarios) {
			if (usTotal.getId().equals(Constantes.usuario.getId())) {
				usuariosTotal.remove(usTotal);
			}
		}
		return usuariosTotal;
	}

	public boolean grabarFollow(Follows follow) {

		Session ses = null;
		Transaction tx = null;

		try {
			ses = UtilHibernate.getSessionFactory().openSession();
			tx = ses.beginTransaction();
			ses.save(follow);
			tx.commit();
			ses.close();
		} catch (Exception ex) {
			tx.rollback();
			if (ses != null)
				ses.close();
		}
		return true;
	}

	public Usuarios obtenerUsuario(String idUsuario) {
		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria cr = ses.createCriteria(Usuarios.class);
		cr.add(Restrictions.eq("id", new Integer(idUsuario)));
		Usuarios user = (Usuarios) cr.uniqueResult();
		ses.close();

		return user;
	}

	// LISTA DE LAS PERSONAS QUE ME SIGUEN
	public List<Usuarios> obtenerListaSigo(Usuarios usuario) {

		Usuarios user = obtenerUsuario(usuario);

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria cr = ses.createCriteria(Follows.class);
		cr.add(Restrictions.eq("usuariosBySeguidos", user));
		@SuppressWarnings("unchecked")
		List<Follows> follows = cr.list();

		List<Usuarios> listaSigo = new ArrayList<Usuarios>();
		for (Follows f : follows) {
			listaSigo.add(f.getUsuariosBySigo());
		}

		return listaSigo;
	}

	// LISTA DE LAS PERSONAS A LAS QUE SIGO
	public List<Usuarios> obtenerListaSeguidores(Usuarios usuario) {

		Usuarios user = obtenerUsuario(usuario);

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria cr = ses.createCriteria(Follows.class);
		cr.add(Restrictions.eq("usuariosBySigo", user));
		@SuppressWarnings("unchecked")
		List<Follows> follows = cr.list();

		List<Usuarios> listaSigo = new ArrayList<Usuarios>();
		for (Follows f : follows) {
			listaSigo.add(f.getUsuariosBySeguidos());
		}

		return listaSigo;
	}

	public boolean unfollow(Usuarios usuarioSigo, Usuarios usuarioSeguido) {
		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria cr = ses.createCriteria(Follows.class);
		cr.add(Restrictions.eq("usuariosBySigo", usuarioSigo));
		cr.add(Restrictions.eq("usuariosBySeguidos", usuarioSeguido));
		Follows follow = (Follows) cr.uniqueResult();
		Constantes.followDao.borrarFollow(follow);

		return true;
	}

	public List<Mensajes> obtenerListaMenciones(Usuarios usuario) {

		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria crMencionesUsuario = ses.createCriteria(Mensajes.class);
		crMencionesUsuario.add(Restrictions.like("mensaje",
				"%@" + usuario.getNick() + "%"));
		@SuppressWarnings("unchecked")
		List<Mensajes> mensajesMenciones = crMencionesUsuario.list();

		return mensajesMenciones;
	}

	public List<String> obtenerTrendingTopic() {

		// Primero obtenemos todos los hashtag del d’a de hoy
		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria crTt = ses.createCriteria(Mensajes.class);
		crTt.add(Restrictions.like("mensaje", "%#%"));
		@SuppressWarnings("unchecked")
		List<Mensajes> mensajesTt = crTt.list();

		// Sacamos la o las palabras que son hashtag
		String mensaje = "";
		int numHashtag = 0;
		List<String> hashtag = new ArrayList<String>();
		for (Mensajes m : mensajesTt) {
			mensaje = m.getMensaje();
			for (int contador = 0; contador < mensaje.length(); contador++) {
				// Por si existe m‡s de un hashtag por tuit
				if (mensaje.charAt(contador) == '#') {
					numHashtag++;
				}
			}
			if (numHashtag == 1) {
				// Controlamos si el œltimo caracter del twit la contiene el
				// hashtag, si es as’, no buscar‡ un espacio en blanco sino por
				// su longitud

				try {
					hashtag.add(mensaje.substring(mensaje.indexOf("#"),
							mensaje.indexOf(" ", mensaje.indexOf("#"))));
				} catch (StringIndexOutOfBoundsException e) {
					hashtag.add(mensaje.substring(mensaje.indexOf("#"),
							mensaje.length()));
				}
			} else if (numHashtag == 2) {
				try {
					hashtag.add(mensaje.substring(mensaje.indexOf("#"),
							mensaje.indexOf(" ", mensaje.indexOf("#"))));
					hashtag.add(mensaje.substring(mensaje.lastIndexOf("#"),
							mensaje.indexOf(" ", mensaje.lastIndexOf("#"))));
				} catch (StringIndexOutOfBoundsException e) {
					hashtag.add(mensaje.substring(mensaje.lastIndexOf("#"),
							mensaje.length()));
				}
			} else {
				// ƒste mŽtodo sirve tambiŽn para los 2 anteriores, pero, al no
				// ser muy —ptimo lo dejamos como œltima opci—n
				String hashtagAux = mensaje;
				int i = 0;
				while (i < numHashtag) {
					try {
						int posicionInicialHashtagAux = hashtagAux.indexOf("#");
						int posicionFinalHashtagAux = hashtagAux.indexOf(" ",
								posicionInicialHashtagAux);
						hashtag.add(hashtagAux.substring(
								posicionInicialHashtagAux,
								posicionFinalHashtagAux));
						hashtagAux = hashtagAux
								.substring(posicionFinalHashtagAux);
					} catch (StringIndexOutOfBoundsException e) {
						int posicionInicialHashtagAux = hashtagAux.indexOf("#");
						hashtag.add(hashtagAux.substring(
								posicionInicialHashtagAux, hashtagAux.length()));
					}
					i++;
				}

			}

			// Se vuelve a poner el contador a 0 para el pr—ximo twit
			numHashtag = 0;
		}
		// Lo ordenamos para facilitarnos el trabajo m‡s tarde
		Collections.sort(hashtag);

		// Creamos un map para meter el hashtag y el numero de veces que
		// aparece
		int contador = 1;
		Map<String, Integer> trendringTopic = new HashMap<String, Integer>();

		for (int i = 0; i < hashtag.size() - 1; i++) {
			// Hacemos una condici—n especial para el œltimo valor del array
			if (i == hashtag.size() - 2) {
				if (!hashtag.get(i).equals(hashtag.get(i + 1))) {
					contador = 1;
					trendringTopic.put(hashtag.get(i + 1), contador);
				} else {
					contador++;
					trendringTopic.put(hashtag.get(i + 1), contador);
				}
			} else {
				if (!hashtag.get(i).equals(hashtag.get(i + 1))) {
					trendringTopic.put(hashtag.get(i), contador);
					contador = 1;
				} else {
					trendringTopic.put(hashtag.get(i), contador);
					contador++;
				}
			}
		}

		Iterator it = trendringTopic.entrySet().iterator();
		// Creamos una lista de la clase TrengingTopic que para ordenarlo
		// posteriormente
		List<TrendingTopic> tt = new ArrayList<TrendingTopic>();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			TrendingTopic trendingTopicClase = new TrendingTopic(e.getKey()
					.toString(), Integer.parseInt(e.getValue().toString()));
			tt.add(trendingTopicClase);
		}
		Collections.sort(tt);

		List<String> trendingTopicString = new ArrayList<String>();
		// Obtenemos los 5 primeros y bual‡!
		for (int i = 0; i < 5; i++) {
			trendingTopicString.add(tt.get(i).getHashtag());
		}

		return trendingTopicString;
	}

	public List<Mensajes> obtenerListaMensajesHashtag(String twitHashtag) {
		Session ses = UtilHibernate.getSessionFactory().openSession();

		Criteria crHashtag = ses.createCriteria(Mensajes.class);
		crHashtag.add(Restrictions.like("mensaje", "%" + twitHashtag + "%"));
		@SuppressWarnings("unchecked")
		List<Mensajes> mensajesHashtag = crHashtag.list();

		return mensajesHashtag;
	}

	public List<MensajesPrivados> obtenerListaMensajesPrivados(Usuarios usuarios) {

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria crMensajesPrivados = ses
				.createCriteria(MensajesPrivados.class);
		crMensajesPrivados.add(Restrictions.eq("usuariosByUsuarioReceptor",
				usuarios));

		@SuppressWarnings("unchecked")
		List<MensajesPrivados> mensajesPrivados = crMensajesPrivados.list();

		return mensajesPrivados;
	}

	public List<Mensajes> obtenerListaMensajesMisTwitsPaginados(
			List<Mensajes> mensajeses, int paginaActual, int twitsPorPagina) {

		List<Mensajes> mensajesPaginadosMisTwits = new ArrayList<Mensajes>();
		paginaActual--;
		int k = paginaActual * twitsPorPagina;
		for (int i = k; i < k + twitsPorPagina; i++) {
			try {
				mensajesPaginadosMisTwits.add(mensajeses.get(i));
			} catch (IndexOutOfBoundsException e) {
				e.printStackTrace();
				break;
			}
		}

		return mensajesPaginadosMisTwits;
	}

	public List<Mensajes> obtenerMisTwitsPaginados(Usuarios usuarios,
			int paginaActual, int twitsPorPagina) {

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria crMensajes = ses.createCriteria(Mensajes.class);
		crMensajes.add(Restrictions.eq("usuarios", usuarios));
		@SuppressWarnings("unchecked")
		List<Mensajes> misTwits = crMensajes.list();

		misTwits = obtenerListaMensajesMisTwitsPaginados(misTwits,
				paginaActual, twitsPorPagina);

		return misTwits;
	}

	public int obtenerNumTwits(Usuarios usuarios) {

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria crMensajes = ses.createCriteria(Mensajes.class);
		crMensajes.add(Restrictions.eq("usuarios", usuarios));
		@SuppressWarnings("unchecked")
		List<Mensajes> misTwits = crMensajes.list();
		ses.close();

		return misTwits.size();
	}

	public int obtenerNumPaginasMisTwits(Usuarios usuario, int twitsPorPagina) {
		int valor = 1;
		try {
			int numTwits = obtenerNumTwits(usuario);
			BigDecimal big = new BigDecimal((double) numTwits
					/ (double) twitsPorPagina);

			big = big.setScale(0, RoundingMode.UP);

			valor = big.intValue();
		} catch (NumberFormatException ex) {

		}
		return valor;
	}

	public void modificarPerfil(Usuarios usuario) {

		Session ses = null;
		Transaction tx = null;
		try {
			ses = UtilHibernate.getSessionFactory().openSession();

			tx = ses.beginTransaction();

			ses.merge(usuario);
			tx.commit();
			ses.close();

		} catch (Exception ex) {
			tx.rollback();
			if (ses != null)
				ses.close();
		}

	}

	public void grabarUsuario(Usuarios usuario) {

		Session ses = null;
		Transaction tx = null;
		try {
			ses = UtilHibernate.getSessionFactory().openSession();
			tx = ses.beginTransaction();
			ses.persist(usuario);
			tx.commit();
			ses.close();
		} catch (Exception ex) {
			tx.rollback();
			if (ses != null)
				ses.close();
		}

	}

}
