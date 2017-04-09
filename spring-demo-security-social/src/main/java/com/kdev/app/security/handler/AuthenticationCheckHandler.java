package com.kdev.app.security.handler;

import org.springframework.security.core.Authentication;

import com.kdev.app.security.userdetails.UserDetails;

public class AuthenticationCheckHandler {
	public boolean checkUserId(Authentication authentication, Long id) {
		UserDetails user = (UserDetails)authentication.getPrincipal();
		if(user.getId().equals(id)){
			return true;
		}
		return false;
	}
}