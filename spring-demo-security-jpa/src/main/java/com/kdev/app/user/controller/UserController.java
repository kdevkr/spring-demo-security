package com.kdev.app.user.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.kdev.app.user.domain.Authority;
import com.kdev.app.user.domain.Authority_Role;
import com.kdev.app.user.domain.Role;
import com.kdev.app.user.domain.User;
import com.kdev.app.user.domain.UserDTO;
import com.kdev.app.user.service.UserService;

@Controller
public class UserController {
	
	private UserService userService;
	
	public UserController(UserService userService) {
		this.userService = userService;
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
		user.setPassword(create.getPassword());
		
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
}
