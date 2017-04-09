package com.kdev.app.social.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.ServletWebRequest;

@Controller
public class SocialSignUpController {
	private static final Logger logger = LoggerFactory.getLogger(SocialSignUpController.class);
	
	@Autowired
	private Facebook facebook;
	
	@RequestMapping(value="/signup/facebook", method=RequestMethod.GET)
	public String signin(ServletWebRequest request, Model model){
		String [] fields = { "id","name","birthday","email","location","hometown","gender","first_name","last_name"};
		org.springframework.social.facebook.api.User facebookUser = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
		logger.info("{}",facebookUser.getId());
		return "register/form";
	}
}
