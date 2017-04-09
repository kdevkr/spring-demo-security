package com.kdev.app.social.userdetails;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.social.security.SocialUser;

import com.kdev.app.security.domain.User;

import lombok.Getter;
import lombok.Setter;

/**
 * @author KDEV
 */
@Getter
@Setter
public class SocialUserDetails extends SocialUser {

	private static final long serialVersionUID = 855226823777023437L;

	private User user;
 
    public SocialUserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }
    
    public SocialUserDetails(User user) {
    	super(user.getEmail(), user.getPassword(), authorities(user));
    	
		this.user = user;
    }
    
	private static Collection<? extends GrantedAuthority> authorities(User user) {
		// TODO Auto-generated method stub
		return user.getAuthorities();
	}
}
