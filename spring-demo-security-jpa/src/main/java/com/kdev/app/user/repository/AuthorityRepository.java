package com.kdev.app.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdev.app.user.domain.Authority;
import com.kdev.app.user.domain.Authority_Role;

public interface AuthorityRepository extends JpaRepository<Authority, Authority_Role> {
	
}