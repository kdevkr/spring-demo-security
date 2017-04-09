package com.kdev.app.security.userdetails;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.web.authentication.rememberme.PersistentRememberMeToken;
import org.springframework.security.web.authentication.rememberme.PersistentTokenRepository;

import com.kdev.app.security.domain.Token;

@Transactional
public class TokenRepositoryImpl  implements PersistentTokenRepository {
	
	@Autowired
	private TokenRepository tokenRepository;
	
	@Override
	public void createNewToken(PersistentRememberMeToken token) {
		// TODO Auto-generated method stub
		Token newToken = new Token();
		newToken.setEmail(token.getUsername());
		newToken.setToken(token.getTokenValue());
		newToken.setLast_used(token.getDate());
		newToken.setSeries(token.getSeries());
		tokenRepository.save(newToken);
	}

	@Override
	public void updateToken(String series, String tokenValue, Date lastUsed) {
		// TODO Auto-generated method stub
		
		Token updateToken = tokenRepository.findOne(series);
		updateToken.setToken(tokenValue);
		updateToken.setLast_used(lastUsed);
		updateToken.setSeries(series);
		tokenRepository.save(updateToken);
	}

	@Override
	public PersistentRememberMeToken getTokenForSeries(String series) {
		// TODO Auto-generated method stub
		Token token = tokenRepository.findOne(series);
		PersistentRememberMeToken persistentRememberMeToken = new PersistentRememberMeToken(token.getEmail(), series, token.getToken(), token.getLast_used());
		return persistentRememberMeToken;
	}

	@Override
	public void removeUserTokens(String username) {
		// TODO Auto-generated method stub
		tokenRepository.deleteByEmail(username);
	}
}
