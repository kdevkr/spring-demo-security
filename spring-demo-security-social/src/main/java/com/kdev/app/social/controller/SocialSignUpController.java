package com.kdev.app.social.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.web.context.request.WebRequest;

@Controller
public class SocialSignUpController {
	private static final Logger logger = LoggerFactory.getLogger(SocialSignUpController.class);
	
	@Autowired
	private Facebook facebook;
	
	@Autowired
	private ProviderSignInUtils providerSignInUtils;
	
	@RequestMapping(value="/signup/facebook", method=RequestMethod.GET)
	@ResponseBody
	public org.springframework.social.facebook.api.User signUpFacebook(ServletWebRequest request, Model model){
		String [] fields = { "id","name","birthday","email","location","hometown","gender","first_name","last_name"};
		org.springframework.social.facebook.api.User facebookUser = facebook.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
		return facebookUser;
	}
	
	@RequestMapping(value="/signup", method=RequestMethod.GET)
	@ResponseBody
	public UserProfile signUp(WebRequest request, Model model){
		Connection<?> connection = providerSignInUtils.getConnectionFromSession(request);
		UserProfile userProfile = null;
		Object Api = connection.getApi();
		if(Api instanceof Facebook){
			// issue (#12) bio field is deprecated for versions v2.8 and higher
			Facebook facebookApi = (Facebook)Api;
			String [] fields = { "id","name","birthday","email","location","hometown","gender","first_name","last_name"};
			org.springframework.social.facebook.api.User facebookUser = facebookApi.fetchObject("me", org.springframework.social.facebook.api.User.class, fields);
			userProfile = new UserProfile(facebookUser.getId(), facebookUser.getName(), facebookUser.getFirstName(), facebookUser.getLastName(), facebookUser.getEmail(), facebookUser.getMiddleName());
		}else{
			userProfile = connection.fetchUserProfile();
		}
		
		return userProfile;
	}
}
