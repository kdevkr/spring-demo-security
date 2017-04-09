package com.kdev.app.social.domain;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

import com.kdev.app.security.domain.User;

import lombok.Data;

/**
 * 
 * @author KDEV
 * Spring Social UserConnection Table
 */
@Entity
@Table(name="USERCONNECTION",
		uniqueConstraints = {
			@UniqueConstraint(columnNames = { "userId", "providerId", "providerUserId" }),
			@UniqueConstraint(columnNames = { "userId", "providerId", "rank" }) 
		})
@Data
public class UserConnection {
	@EmbeddedId
	private UserConnectionPK primaryKey;
	private String accessToken;
	private String displayName;
	private Long expireTime;
	private String imageUrl;
	private String profileUrl;
	private int rank;
	private String refreshToken;
	private String secret;
}