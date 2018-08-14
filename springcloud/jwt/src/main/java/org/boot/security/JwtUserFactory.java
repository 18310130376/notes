package org.boot.security;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUserFactory {

	private JwtUserFactory() {
	}

	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getUsername(), user.getPassword(), user.getEmail(),
				mapToGrantedAuthorities(user.getRoles()),user.getLastPasswordResetDate());
	}

	private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
//		return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
		
		List<GrantedAuthority> list = new ArrayList<>();
		if(authorities != null && authorities.size() >0) {
			for(String role : authorities) {
				list.add(new SimpleGrantedAuthority("ROLE_"+role));
			}
		}
		return list;
	}
}
