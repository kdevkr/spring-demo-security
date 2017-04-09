package com.kdev.app.social.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.servlet.view.RedirectView;

public class SocialConnectController extends ConnectController {
	
	private static final Logger logger = LoggerFactory.getLogger(SocialConnectController.class);
	
	public SocialConnectController(ConnectionFactoryLocator connectionFactoryLocator,
			ConnectionRepository connectionRepository) {
		super(connectionFactoryLocator, connectionRepository);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected String connectedView(String providerId) {
		// TODO Auto-generated method stub
		if(providerId.equals("facebook"))
			return "redirect:/signup/facebook";
		else if(providerId.equals("kakao"))
			return "redirect:/signup/kakao";
		return super.connectedView(providerId);
	}
}