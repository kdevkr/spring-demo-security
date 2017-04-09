package com.kdev.app.security.config;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

/**
 * <pre>
 * com.kdev.app.security.config
 * AuthUserContollerAdvice.java
 * </pre>
 * @author KDEV
 * @version 
 * @created 2017. 3. 23.
 * @updated 2017. 3. 23.
 * @history -
 * ==============================================
 *  ref : https://kielczewski.eu/2014/12/spring-boot-security-application/
 * ==============================================
 */
@ControllerAdvice
public class AuthUserContollerAdvice {
	
	@ModelAttribute("auth")
	public UserDetails getAuthUser(Authentication authentication){
		return (authentication == null) ? null : (UserDetails) authentication.getPrincipal();
	}
}
