package mvieghofer.github.com.persitence.dao;

import java.util.Collection;

import mvieghofer.github.com.persitence.model.User;

public interface UserDao {
	User findById(Long key);
	Collection<User> findAll();
	void insertUser(User p);
	User updateUser(User p);
	void deleteUser(User p);
}
