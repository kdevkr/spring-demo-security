package com.kdev.app.social.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.social.UserIdSource;
import org.springframework.social.config.annotation.SocialConfigurerAdapter;
import org.springframework.social.connect.Connection;
import org.springframework.social.connect.ConnectionFactoryLocator;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.UsersConnectionRepository;
import org.springframework.social.connect.jdbc.JdbcUsersConnectionRepository;
import org.springframework.social.connect.web.ConnectController;
import org.springframework.social.connect.web.ProviderSignInController;
import org.springframework.social.connect.web.ProviderSignInUtils;
import org.springframework.social.facebook.api.Facebook;
import org.springframework.social.security.AuthenticationNameUserIdSource;
import org.springframework.social.security.SocialUserDetailsService;

import com.kdev.app.social.controller.SocialConnectController;
import com.kdev.app.social.service.SpringSecuritySignInAdapter;
import com.kdev.app.social.userdetails.SocialUserDetailsServiceImpl;

/**
 * @author KDEV
 * @history -
 * ==============================================
 *	Spring Social Configuration Class
 * ==============================================
 */
@Configuration
public class SocialConfig extends SocialConfigurerAdapter {

	@Autowired
	private DataSource dataSource;
	
	@Override
	public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {
		// TODO Auto-generated method stub
		JdbcUsersConnectionRepository repository = new JdbcUsersConnectionRepository(dataSource, connectionFactoryLocator, Encryptors.noOpText());
		return repository;
	}
	
	@Override
	public UserIdSource getUserIdSource() {
		return new AuthenticationNameUserIdSource();
	}
	
	/*
	 * http://docs.spring.io/spring-social/docs/current/reference/htmlsingle/#creating-connections-with-connectcontroller
	 * The path pattern that ConnectController handles is "/connect/{providerId}". 
	 */
	
	@Bean
    public ConnectController connectController(
                ConnectionFactoryLocator connectionFactoryLocator,
                ConnectionRepository connectionRepository) {
		SocialConnectController controller = new SocialConnectController(connectionFactoryLocator, connectionRepository);
	        return controller;
    }
	
	@Bean
	public ProviderSignInController providerSignInController(
	            ConnectionFactoryLocator connectionFactoryLocator,
	            UsersConnectionRepository usersConnectionRepository) {
	    return new ProviderSignInController(
	        connectionFactoryLocator,
	        usersConnectionRepository,
	        new SpringSecuritySignInAdapter());
	}
	
	@Bean 
	public SocialUserDetailsService SocialUserDetailsService(){
		return new SocialUserDetailsServiceImpl();
	}
	
	@Bean
	public ProviderSignInUtils providerSignInUtils(ConnectionFactoryLocator connectionFactoryLocator, UsersConnectionRepository usersConnectionRepository) {
		return new ProviderSignInUtils(connectionFactoryLocator, usersConnectionRepository);
	}
	
	@Bean
	@Scope(value="request", proxyMode=ScopedProxyMode.INTERFACES)
	public Facebook facebook(ConnectionRepository repository) {
		Connection<Facebook> connection = repository.findPrimaryConnection(Facebook.class);
		return connection != null ? connection.getApi() : null;
	}
}