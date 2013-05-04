package mvieghofer.github.com.persitence;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;

public class PersistenceManagerUtil {
	private static final PersistenceManagerFactory instance = JDOHelper.getPersistenceManagerFactory("transactions-optional");

	public static PersistenceManager getPersistenceManager() {
		return instance.getPersistenceManager();
	}
}
