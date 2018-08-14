package org.boot.security;

public interface AuthService {
	
	   User register(User userToAdd);
	    String login(String username, String password);
	    String refresh(String oldToken);

}
