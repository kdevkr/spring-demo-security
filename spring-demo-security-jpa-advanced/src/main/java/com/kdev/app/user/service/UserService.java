package com.kdev.app.user.service;

import java.util.Collection;

import org.springframework.security.core.Authentication;

import com.kdev.app.user.domain.Authority;
import com.kdev.app.user.domain.User;

public interface UserService {
	public User findOne(Long id);
	public boolean exist(String email);
	public User save(User user);
	public User update(User user);
	public void delete(User user);
	public Collection<User> findAll();
	public void addAuthorities(Collection<Authority> authorities);
	public void resetAuthorities(Collection<Authority> authorities);
	public boolean checkAuthentication(Authentication authentication, Long id);
}
