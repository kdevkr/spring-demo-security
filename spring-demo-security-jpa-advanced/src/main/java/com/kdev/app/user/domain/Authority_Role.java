package com.kdev.app.user.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Authority_Role implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Column(name = "ID")
	private Long id;

	@Enumerated(EnumType.STRING)
	@Column(name="ROLE")
	private Role role;
}