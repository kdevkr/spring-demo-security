package com.kdev.app.security.handler;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import com.kdev.app.security.userdetails.UserDetails;

/**
 * <pre>
 * com.kdev.app.security.config
 * AuthenticationSuccessHandlerImpl.java
 * </pre>
 * @author KDEV
 * @version 
 * @created 2017. 3. 28.
 * @updated 2017. 3. 30.
 * @history -
 * ==============================================
 *	인 메모리 인증 객체는 마스터 관리자 계정으로써 관리 페이지에만 접근할 수 있다.
 *	따라서, 로그인 시 분기를 목적으로 UserDetails 객체인지를 구분하여 처리한다.
 * ==============================================
 */
public class AuthenticationSuccessHandlerImpl extends SavedRequestAwareAuthenticationSuccessHandler  {
	private static final Logger logger = LoggerFactory.getLogger(AuthenticationSuccessHandlerImpl.class);

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		// TODO Auto-generated method stub
		if(authentication.getPrincipal() instanceof UserDetails){
			//회원가입된 인증 정보의 경우에는 메인페이지로 이동한다.
			response.sendRedirect("/?login");
		}else{
			//인메모리 인증 객체는 관리페이지로 이동한다.
			response.sendRedirect("/admin");
		}
	}

}
