package twitter.util;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;


/**
 * @author Rafael de los Santos Guirado
 *
 */
public class UtilHibernate {

	
	public static SessionFactory getSessionFactory() {
		try {
			return new Configuration().configure().buildSessionFactory();
			} catch (Exception e) {
			throw new IllegalStateException(
					"Could not locate SessionFactory in JNDI");
		}
	}
	
	
}
