package com.kdev.app.social.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.UserProfile;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.connect.web.SignInAdapter;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.NativeWebRequest;

@Service
public class SpringSecuritySignInAdapter implements SignInAdapter {
	
	private static final Logger logger = LoggerFactory.getLogger(SpringSecuritySignInAdapter.class);
	
    public String signIn(String localUserId, Connection<?> connection, NativeWebRequest request) {

		UserProfile userProfile = connection.fetchUserProfile();
		logger.info("{}",userProfile);
        return null;
    }
}