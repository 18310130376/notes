package org.boot.security;

import java.util.HashMap;
import java.util.Map;

public class UserDB {

	public static Map<String, User> userList = new HashMap<>();

	public static  User createUser(User user) {

		if (!userList.containsKey(user.getUsername())) {
			userList.put(user.getUsername(), user);
		}
		return user;
	}

	public static User findByUsername(String username) {
		return userList.get(username);
	}

}
