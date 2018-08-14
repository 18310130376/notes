package org.boot.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
	private AuthenticationManager authenticationManager;
	private UserDetailsService userDetailsService;
	private JwtTokenUtil jwtTokenUtil;

	@Value("${jwt.tokenHead}")
	private String tokenHead;

	@Autowired
	public AuthServiceImpl(AuthenticationManager authenticationManager, UserDetailsService userDetailsService,
			JwtTokenUtil jwtTokenUtil) {
		this.authenticationManager = authenticationManager;
		this.userDetailsService = userDetailsService;
		this.jwtTokenUtil = jwtTokenUtil;
	}

	@Override
	public User register(User userToAdd) {
		final String username = userToAdd.getUsername();

		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		final String rawPassword = userToAdd.getPassword();
		userToAdd.setPassword(encoder.encode(rawPassword));
		userToAdd.setLastPasswordResetDate(new Date());
		userToAdd.setUsername(username);
		return UserDB.createUser(userToAdd);
	}

	@Override
	public String login(String username, String password) {
		String token = null;
		try {
			UsernamePasswordAuthenticationToken upToken = new UsernamePasswordAuthenticationToken(username, password);
			final Authentication authentication = authenticationManager.authenticate(upToken);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			final UserDetails userDetails = userDetailsService.loadUserByUsername(username);
			token = jwtTokenUtil.generateToken(userDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return token;
	}

	@Override
	public String refresh(String oldToken) {
		final String token = oldToken.substring(tokenHead.length());
		String username = jwtTokenUtil.getUsernameFromToken(token);
		JwtUser user = (JwtUser) userDetailsService.loadUserByUsername(username);
		if (jwtTokenUtil.canTokenBeRefreshed(token, user.getLastPasswordResetDate())) {
			return jwtTokenUtil.refreshToken(token);
		}
		return null;
	}
}
