package org.boot.security;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

	@Override
	public void commence(HttpServletRequest request,
			HttpServletResponse response, AuthenticationException authException)
			throws IOException {
		// This is invoked when user tries to access a secured REST resource
		// without
		// supplying any credentials
		// We should just send a 401 Unauthorized response because there is no
		// 'login
		// page' to redirect to
		if (isAjaxRequest(request)) {
			response.sendError(HttpServletResponse.SC_UNAUTHORIZED,
					"Unauthorized");
		} else {
			response.sendRedirect("/login");
		}
	}

	public static boolean isAjaxRequest(HttpServletRequest request) {
		String ajaxFlag = request.getHeader("X-Requested-With");
		return ajaxFlag != null && "XMLHttpRequest".equals(ajaxFlag);
	}
}
