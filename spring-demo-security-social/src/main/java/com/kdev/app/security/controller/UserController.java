package com.kdev.app.security.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kdev.app.security.domain.Authority;
import com.kdev.app.security.domain.Authority_Role;
import com.kdev.app.security.domain.Role;
import com.kdev.app.security.domain.User;
import com.kdev.app.security.domain.UserDTO;
import com.kdev.app.security.service.UserService;
import com.kdev.app.security.userdetails.UserDetails;

@Controller
public class UserController {
	
	private static final Logger logger = LoggerFactory.getLogger(UserController.class);
	
	private UserService userService;
	private PasswordEncoder passwordEncoder;
	
	public UserController(UserService userService, PasswordEncoder passwordEncoder) {
		this.userService = userService;
		this.passwordEncoder = passwordEncoder;
	}
	
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String register(Model model){
		return "register";
	}
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String register(@ModelAttribute @Valid UserDTO.Create create){
		
		User user = new User();
		user.setEmail(create.getEmail());
		user.setNickname(create.getNickname());
		user.setPassword(passwordEncoder.encode(create.getPassword()));
		
		user.setEnabled(true);
		user.setAccountNonExpired(true);
		user.setAccountNonLocked(true);
		user.setCredentialsNonExpired(true);
		
		user = userService.save(user);
		
		List<Authority> authorities = new ArrayList<Authority>();
		
		for(String r : create.getRole()){
			Authority_Role authority_role = new Authority_Role(user.getId(), Role.valueOf(r));
			Authority authority = new Authority();
			authority.setAuthority_role(authority_role);
			
			authorities.add(authority);
		}
		
		userService.addAuthorities(authorities);
		
		return "login";
	}
	
	@RequestMapping(value="/users/{userId}", method=RequestMethod.GET)
	public String update(Model model){
		return "update";
	}
	
	@RequestMapping(value="/users/{userId}", method=RequestMethod.POST)
	public String update(@PathVariable("userId") Long id, @ModelAttribute @Valid UserDTO.Update updateUser, Model model){
		User user = userService.findOne(id);
		
		if(!passwordEncoder.matches(updateUser.getPassword(), user.getPassword())){
			throw new RuntimeException("Not password equals...");
		}
		user.setNickname(updateUser.getNickname());
	    user = userService.update(user);
		
		SecurityContext context = SecurityContextHolder.getContext();
		Authentication auth = new UsernamePasswordAuthenticationToken(new UserDetails(user), user.getPassword(), user.getAuthorities());
		context.setAuthentication(auth);
		return "redirect:/";
	}
}
