package com.kdev.app.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.kdev.app.security.domain.Authority;
import com.kdev.app.security.domain.Authority_Role;

@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Authority_Role> {
	
}
