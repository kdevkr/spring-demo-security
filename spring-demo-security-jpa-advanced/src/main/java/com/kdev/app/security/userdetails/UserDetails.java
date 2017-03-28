package com.kdev.app.security.userdetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.kdev.app.user.domain.User;

public class UserDetails extends org.springframework.security.core.userdetails.User {
	
	private static final long serialVersionUID = -4855890427225819382L;
	
	private Long id;
	private String nickname;
	private String email;
	private Date createdAt;
	
	
	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
	
	public UserDetails(User user){
		super(user.getEmail(), user.getPassword(), user.isAccountNonExpired(), user.isAccountNonLocked(), user.isCredentialsNonExpired(), user.isEnabled(), authorities(user));
		
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.email = user.getEmail();
		this.createdAt = user.getCreatedAt();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	private static Collection<? extends GrantedAuthority> authorities(User user) {
		// TODO Auto-generated method stub
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		user.getAuthorities().forEach(a -> {
			authorities.add(new SimpleGrantedAuthority(a.getAuthority()));
		});
		return authorities;
	}

	public UserDetails(String username, String password, Collection<? extends GrantedAuthority> authorities,
			String nickname) {
		super(username, password, authorities);
		this.nickname = nickname;
		this.email = username;
	}

	public UserDetails(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		// TODO Auto-generated constructor stub
	}
}