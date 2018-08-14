package org.boot.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class UserController {

	@PreAuthorize("hasRole('ADMIN')")
	@RequestMapping(value="getUsers",method = RequestMethod.GET)
    public List<User> getUsers() {
        return new ArrayList<>();
    }
	
	@PreAuthorize("hasRole('USER')")
	@RequestMapping(value="getUsersList",method = RequestMethod.GET)
    public List<User> getUsersList() {
        return new ArrayList<>();
    }
	
	
	 @PostAuthorize("returnObject.username == principal.username or hasRole('ROLE_ADMIN')")
	 @RequestMapping(value = "/principalUser",method = RequestMethod.GET)
	 public User getUserByUsername(@RequestParam(value="username") String username) {
	        return UserDB.findByUsername(username);
	 }
}
