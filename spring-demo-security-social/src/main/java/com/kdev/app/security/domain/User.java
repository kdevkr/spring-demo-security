package com.kdev.app.security.domain;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import com.kdev.app.social.domain.UserConnection;
import com.kdev.app.social.userdetails.SocialProvider;

import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class User {
	@Id
	@Column(length=255)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(length=255,	unique=true)
	private String email;
	
	@Column(length=255)
	private String nickname;
	
	@Column(length=255)
	private String phoneNumber;
	
	@Column(length=255)
	private String thumbnail;
	
	@Column(length=255)
	private String password;
	
	@Column
	private boolean accountNonExpired = true;
	
	@Column
	private boolean accountNonLocked = true;
	
	@Column
	private boolean credentialsNonExpired = true;
	
	@Column
	private boolean enabled;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@OneToMany(mappedBy="authority_role.id", fetch=FetchType.LAZY, cascade=CascadeType.REMOVE)
	private Set<Authority> authorities;
	
	@OneToMany(fetch=FetchType.LAZY)
	private Set<UserConnection> socialConnections;
	
}