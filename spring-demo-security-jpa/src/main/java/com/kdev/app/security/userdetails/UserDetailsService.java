package com.kdev.app.security.userdetails;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.kdev.app.user.domain.User;
import com.kdev.app.user.repository.UserRepository;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {
	
	@Autowired
	private UserRepository userRepository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		if(user == null){
			throw new UsernameNotFoundException(email);
		}
		com.kdev.app.security.userdetails.UserDetails userDetails = new com.kdev.app.security.userdetails.UserDetails(user);
		return userDetails;
	}
}
