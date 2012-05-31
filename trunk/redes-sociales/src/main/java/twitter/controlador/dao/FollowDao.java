package twitter.controlador.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import twitter.modelo.Follows;
import twitter.modelo.Usuarios;
import twitter.util.UtilHibernate;

public class FollowDao {

	public void borrarFollow(Follows follow) {
		Session ses = null;

		try {
			ses = UtilHibernate.getSessionFactory().openSession();
			Follows f = (Follows) ses.merge(follow);
			ses.getTransaction().begin();
			ses.delete(f);
			ses.getTransaction().commit();

		} catch (Exception ex) {
			ses.getTransaction().rollback();
			System.out.println(ex.getMessage());
			if (ses != null)
				ses.close();
		}
	}

	public boolean isExisteFollow(Usuarios usuarioSeguido, Usuarios usuarioSigo) {

		boolean existe = false;

		Session ses = UtilHibernate.getSessionFactory().openSession();
		Criteria crFollow = ses.createCriteria(Follows.class);
		crFollow.add(Restrictions.eq("usuariosBySeguidos", usuarioSeguido));
		crFollow.add(Restrictions.eq("usuariosBySigo", usuarioSigo));
		
		@SuppressWarnings("unchecked")
		List<Follows> follow = new ArrayList<Follows>(crFollow.list());
		ses.close();

		if (!follow.isEmpty()) {
			existe = true;
		}
		return existe;
	}
}
