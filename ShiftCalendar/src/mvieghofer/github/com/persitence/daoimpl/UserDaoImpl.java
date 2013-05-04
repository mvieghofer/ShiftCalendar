package mvieghofer.github.com.persitence.daoimpl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import mvieghofer.github.com.persitence.PersistenceManagerUtil;
import mvieghofer.github.com.persitence.dao.UserDao;
import mvieghofer.github.com.persitence.model.User;

public class UserDaoImpl implements UserDao {

	@Override
	public User findById(Long key) {
		PersistenceManager manager = PersistenceManagerUtil
				.getPersistenceManager();
		User user = null;
		try {
			user = manager.getObjectById(User.class, key);
		} finally {
			manager.close();
		}
		return user;
	}

	@Override
	public Collection<User> findAll() {
		PersistenceManager manager = PersistenceManagerUtil
				.getPersistenceManager();
		List<User> users = new ArrayList<User>();
		try {
			Query query = manager.newQuery(User.class);
			Collection<User> queryResult = (Collection<User>) query.execute();
			for (User u : queryResult) {
				users.add(u);
			}
		} finally {
			manager.close();
		}
		return users;
	}

	@Override
	public void insertUser(User user) {
		PersistenceManager manager = PersistenceManagerUtil
				.getPersistenceManager();
		try {
			manager.makePersistent(user);
		} finally {
			manager.close();
		}
	}

	@Override
	public User updateUser(User user) {
		User newUser = null;

		PersistenceManager manager = PersistenceManagerUtil
				.getPersistenceManager();
		try {
			manager.currentTransaction().begin();

			newUser = manager.getObjectById(User.class, user.getKey());
			newUser.setName(user.getName());
			newUser.setEmail(user.getEmail());
			newUser.setRefreshToken(user.getRefreshToken());
			newUser.setAccessToken(user.getAccessToken());
			manager.makePersistent(newUser);

			manager.currentTransaction().commit();
		} catch (RuntimeException ex) {
			manager.currentTransaction().rollback();
			throw ex;
		} finally {
			manager.close();
		}
		return newUser;
	}

	@Override
	public void deleteUser(User p) {
		PersistenceManager manager = PersistenceManagerUtil
				.getPersistenceManager();
		User user = null;
		try {
			manager.currentTransaction().begin();
			user = manager.getObjectById(User.class, p.getKey());
			if (user != null) {
				manager.deletePersistent(user);
			}
			manager.currentTransaction().commit();
		} catch (RuntimeException ex) {
			manager.currentTransaction().rollback();
			throw ex;
		} finally {

			manager.close();
		}
	}

}
