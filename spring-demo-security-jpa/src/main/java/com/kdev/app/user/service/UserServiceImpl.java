package com.kdev.app.user.service;

import java.util.Collection;

import javax.transaction.Transactional;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import com.kdev.app.security.userdetails.UserDetails;
import com.kdev.app.user.domain.Authority;
import com.kdev.app.user.domain.User;
import com.kdev.app.user.repository.AuthorityRepository;
import com.kdev.app.user.repository.UserRepository;

@Service
@Transactional
public class UserServiceImpl implements UserService {
	
	private UserRepository userRepository;
	private AuthorityRepository authorityRepository;

	public UserServiceImpl(UserRepository userRepository, AuthorityRepository authorityRepository) {
		this.userRepository = userRepository;
		this.authorityRepository = authorityRepository;
	}

	@Override
	public User findOne(Long id) {
		// TODO Auto-generated method stub
		return userRepository.findOne(id);
	}

	@Override
	public boolean exist(String email) {
		// TODO Auto-generated method stub
		User user = userRepository.findByEmail(email);
		if(user != null){
			return false;
		}
		return true;
	}

	@Override
	public User save(User user) {
		// TODO Auto-generated method stub
		return userRepository.saveAndFlush(user);
	}

	@Override
	public User update(User user) {
		// TODO Auto-generated method stub
		return userRepository.saveAndFlush(user);
	}

	@Override
	public void delete(User user) {
		// TODO Auto-generated method stub
		userRepository.delete(user);
	}

	@Override
	public Collection<User> findAll() {
		// TODO Auto-generated method stub
		return userRepository.findAll();
	}

	@Override
	public void addAuthorities(Collection<Authority> authorities) {
		// TODO Auto-generated method stub
		authorityRepository.save(authorities);
	}

	@Override
	public void resetAuthorities(Collection<Authority> authorities) {
		// TODO Auto-generated method stub
		authorityRepository.delete(authorities);
	}

	@Override
	public boolean checkAuthentication(Authentication authentication, Long id) {
		// TODO Auto-generated method stub
		UserDetails userDetails = (UserDetails)authentication.getPrincipal();
		if(userDetails.getId().equals(id)){
			return true;
		}
		return false;
	}

}
