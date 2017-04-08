package com.kdev.app.security.userdetails;

import org.springframework.data.jpa.repository.JpaRepository;

import com.kdev.app.security.domain.Token;

public interface TokenRepository extends JpaRepository<Token, String> {
	public void deleteByEmail(String email);
}
