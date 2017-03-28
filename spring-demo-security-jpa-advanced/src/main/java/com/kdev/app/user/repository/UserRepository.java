package com.kdev.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdev.app.user.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(@Param("email") String email);
}