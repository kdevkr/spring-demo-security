package com.kdev.app.security.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.kdev.app.security.userdetails.UserDetailsService;

/**
 * <pre>
 * com.kdev.app.security.config
 * SecurityConfig.java
 * </pre>
 * @author KDEV
 * @version 
 * @created 2017. 3. 21.
 * @updated 2017. 3. 21.
 * @history -
 * ==============================================
 *	
 * ==============================================
 */
@Configuration
/*
 * ==============================================
 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#method-security-expressions
 * ==============================================
 */
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	private static final Logger logger = LoggerFactory.getLogger(WebSecurityConfig.class);
	private static final String REMEMBER_ME = "kdevkr";
	
	@Autowired
	private UserDetailsService userDetailsService;
	/*
	 * ==============================================
	 *	리소스 관련 파일에 대해서는 시큐리티 적용을 무시하는 설정을 합니다.
	 * ==============================================
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/static/**","/resources/**");
	}

	/*
	 * ==============================================
	 *	스프링 시큐리티 설정을 커스터마이징 합니다.
	 * ==============================================
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.httpBasic();
		
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#authorize-requests
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#el-access
		 * ==============================================
		 */
		http.authorizeRequests()
			.antMatchers("/users/{userId}/**").access("@authenticationCheckHandler.checkUserId(authentication,#userId)")
			.antMatchers("/admin/authorities").access("hasRole('ADMIN_MASTER') or hasRole('ADMIN') and hasRole('AUTHORITY')")
			.antMatchers("/admin/**").hasAnyRole("ADMIN_MASTER", "ADMIN")
			.antMatchers("/register/**").hasRole("ANONYMOUS")
			.antMatchers("/api/**").hasRole("API")
			.antMatchers(HttpMethod.GET, "/blog/{userId}/admin").access("@authenticationCheckHandler.checkUserId(authentication,#userId) and hasRole('BLOG')")
			.antMatchers("/**").hasAnyRole("ANONYMOUS", "ADMIN", "USER");
		
		http.formLogin()
			.loginPage("/login")
			.usernameParameter("email")
			.passwordParameter("password")
			.successHandler(successHandler())
			.failureHandler(failureHandler())
			//.defaultSuccessUrl("/?login")
			//.failureUrl("/login?error")
			.permitAll();
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-logout-handler
		 * ==============================================
		 */
		
		http.logout()
		.logoutUrl("/logout")
		.logoutSuccessUrl("/")
		// Generally, in order to customize logout functionality, you can add LogoutHandler and/or LogoutSuccessHandler implementations
		//.logoutSuccessHandler(logoutSuccessHandler) // is called after a successful logout by the LogoutFilter
		//.addLogoutHandler(logoutHandler)
		.invalidateHttpSession(true); // This is true by default.
		
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#csrf-cookie
		 * ==============================================
		 */
		http.csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#remember-me-impls
		 * ==============================================
		 */
		http.rememberMe().rememberMeParameter("remember-me").key(REMEMBER_ME);
		
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#ns-session-mgmt
		 * ==============================================
		 */
		http.sessionManagement()
		.sessionFixation().migrateSession().invalidSessionUrl("/login?invalid")
		.maximumSessions(1).expiredUrl("/login?expired");
		
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#cors
		 * ==============================================
		 */
		http.cors();
		
		/*
		 * ==============================================
		 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#headers
		 * ==============================================
		 */
		
		http.headers().frameOptions().sameOrigin().httpStrictTransportSecurity().disable();
	} 

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		//https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#jc-authentication-inmemory
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN_MASTER","AUTHORITY");
		//remember-me에서 jdbc userDetailsService를 이용하기에 오류 발생한다.
		
		//2017-03-24 : 애초에 InMemoryUserDetailsManager에서 UserDetails 구현체를 가지고 새로운 User 객체를 생성하기 때문에 커스터마이징된 UserDetails 구현체를 사용할 수 없다.
		
		//JDBC를 활용한 사용자 계정 등록하기
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder(){
		return new BCryptPasswordEncoder();
	}
	
	/*
	 * ==============================================
	 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#tokenbasedremembermeservices
	 * ==============================================
	 */
	@Bean
	public TokenBasedRememberMeServices tokenBasedRememberMeServices(){
		return new TokenBasedRememberMeServices(REMEMBER_ME, userDetailsService);
	}
	
	/*
	 * ==============================================
	 *	https://docs.spring.io/spring-security/site/docs/current/reference/htmlsingle/#el-access-web-path-variables
	 * ==============================================
	 */
	@Bean
	public AuthenticationCheckHandler authenticationCheckHandler(){
		return new AuthenticationCheckHandler();
	}
	
	@Bean
	public AuthenticationSuccessHandler successHandler(){
		return new AuthenticationSuccessHandlerImpl();
	}
	
	@Bean
	public AuthenticationFailureHandler failureHandler(){
		return new AuthenticationFailureHandlerImpl();
	}
}
