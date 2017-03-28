package com.kdev.app;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class HomeController {
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public String home(Model model){
		return "home";
	}
	
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String login(Model model){
		return "login";
	}
}
