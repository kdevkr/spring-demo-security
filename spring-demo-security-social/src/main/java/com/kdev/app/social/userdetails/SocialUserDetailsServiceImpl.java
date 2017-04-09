package com.kdev.app.social.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.social.security.SocialUserDetails;
import org.springframework.social.security.SocialUserDetailsService;

public class SocialUserDetailsServiceImpl implements SocialUserDetailsService {

	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	public SocialUserDetails loadUserByUserId(String userId) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		return (SocialUserDetails)userDetailsService.loadUserByUsername(userId);
	}
}
