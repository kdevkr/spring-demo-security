package com.kdev.app;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
	
	@RequestMapping(value="/")
	public String index(Model model){
		return "index";
	}
	
	@RequestMapping(value="/login")
	public String login(Model model, HttpSession session){
		return "login";
	}
	
	@RequestMapping(value="/register")
	public String register(Model model){
		return "register";
	}
	
	@RequestMapping(value="/admin")
	public String admin(Model model){
		return "admin";
	}
}
