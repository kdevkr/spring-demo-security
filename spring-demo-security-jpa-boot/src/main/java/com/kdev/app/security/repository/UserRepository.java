package com.kdev.app.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.kdev.app.security.domain.User;

/**
 * <pre>
 * com.kdev.app.user.repository
 * UserRepository.java
 * </pre>
 * @author KDEV
 * @version 
 * @created 2017. 3. 21.
 * @updated 2017. 3. 21.
 * @history -
 * ==============================================
 *
 * ==============================================
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	public User findByEmail(@Param("email") String email);
}
