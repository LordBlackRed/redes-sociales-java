package twitter.controlador.dao;

import java.util.Date;

import org.hibernate.Session;
import org.hibernate.Transaction;

import twitter.modelo.Mensajes;
import twitter.modelo.MensajesPrivados;
import twitter.modelo.Usuarios;
import twitter.util.UtilHibernate;

public class MensajeDao {

	// Le pasamos un usuario debido a que, en el usuario de sesi—n s—lo se
	// guardan los campos nombre y pass, y no la id
	public boolean grabarMensaje(Mensajes mensaje, Usuarios usuario) {

		System.out.println(usuario.getId());

		mensaje.setFecha(new Date());
		Session ses = null;
		Transaction tx = null;
		try {
			ses = UtilHibernate.getSessionFactory().openSession();

			tx = ses.beginTransaction();

			ses.save(mensaje);
			tx.commit();
			ses.close();
			return true;

		} catch (Exception ex) {
			tx.rollback();
			if (ses != null)
				ses.close();
			return false;
		}
	}

	public void grabarMensajePrivado(MensajesPrivados mensajePrivado) {

		Session ses = null;
		Transaction tx = null;
		try {
			ses = UtilHibernate.getSessionFactory().openSession();

			tx = ses.beginTransaction();

			ses.save(mensajePrivado);
			tx.commit();
			ses.close();

		} catch (Exception ex) {
			tx.rollback();
			if (ses != null)
				ses.close();
		}

	}
}
