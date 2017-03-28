package com.kdev.app.user.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import org.springframework.security.core.GrantedAuthority;

import lombok.Data;

@Entity
@Table(name="authorities")
@Data
public class Authority implements GrantedAuthority {
	/**
	 * 
	 */
	private static final long serialVersionUID = 657425269815064052L;

	@EmbeddedId
	private Authority_Role authority_role;
	
	@Override
	public String getAuthority() {
		// TODO Auto-generated method stub
		return authority_role.getRole().name();
	}
}