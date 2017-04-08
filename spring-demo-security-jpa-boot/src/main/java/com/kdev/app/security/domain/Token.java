package com.kdev.app.security.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

@Entity
@Table(name="tokens")
@Data
public class Token {
	@Column(nullable=false)
	private String email;
	@Id
	private String series;
	@Column(nullable=false)
	private String token;
	@Temporal(TemporalType.TIMESTAMP)
	private Date last_used;
}
