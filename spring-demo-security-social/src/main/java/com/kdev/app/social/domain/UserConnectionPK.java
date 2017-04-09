package com.kdev.app.social.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;

import lombok.Data;

@Embeddable
@Data
public class UserConnectionPK implements Serializable {
	private static final long serialVersionUID = 2246146784504184874L;
	private String userId;
	private String providerId;
	private String providerUserId;
}