package ink.eck.news.dao;

import java.util.List;

import ink.eck.news.entity.User;

public interface UserDao {
	int addUser(User u);
	int updateUser(User u);
	User checkUser(User u);
	int deleteUserById(int id);
	List<User> getAllUser();
	
}
