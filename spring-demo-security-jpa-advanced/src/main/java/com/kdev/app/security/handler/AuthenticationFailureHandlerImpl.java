package com.kdev.app.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class AuthenticationFailureHandlerImpl extends SimpleUrlAuthenticationFailureHandler {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationFailureHandlerImpl.class);
	
	public AuthenticationFailureHandlerImpl() {
		this.setDefaultFailureUrl("/login?error");
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		logger.info("Login Failed... - {}",request.getParameter("email"));
		super.onAuthenticationFailure(request, response, exception);
	}

}