package com.kdev.app.user.domain;

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

import lombok.Data;

@Data
@Table(name = "users")
@Entity
public class User {
	@Id
	@Column(name = "ID", length=255)
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	@Column(name = "EMAIL", length=255,	unique=true)
	private String email;
	
	@Column(name = "NICKNAME", length=255)
	private String nickname;
	
	@Column(name = "PASSWORD", length=255)
	private String password;
	
	@Column(name = "ANONEXPRIED")
	private boolean accountNonExpired = true;
	
	@Column(name = "ANONLOCKED")
	private boolean accountNonLocked = true;
	
	@Column(name = "CNONEXPRIED")
	private boolean credentialsNonExpired = true;
	
	@Column(name = "ENABLED")
	private boolean enabled;
	
	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="CREATEADAT", insertable=false, updatable=false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
	private Date createdAt;
	
	@OneToMany(mappedBy="authority_role.id", fetch=FetchType.EAGER, cascade=CascadeType.REMOVE)
	private Set<Authority> authorities;
}