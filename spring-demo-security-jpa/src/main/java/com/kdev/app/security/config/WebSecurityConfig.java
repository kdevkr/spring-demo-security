package com.kdev.app.security.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;

import com.kdev.app.security.userdetails.UserDetailsService;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	private static final String REMEMBER_ME_KEY = "REMEMBER_ME_KEY";
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailsService);
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.httpBasic()
			.and()
			.authorizeRequests()
				.antMatchers("/register/**").hasRole("ANONYMOUS")
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/")
				.failureUrl("/login?error")
				.permitAll()
			.and()
			.rememberMe().rememberMeParameter("remember-me").key(REMEMBER_ME_KEY);
	}
	
	@Bean
	public TokenBasedRememberMeServices TokenBasedRememberMeServices(){
		TokenBasedRememberMeServices tokenBasedRememberMeServices = new TokenBasedRememberMeServices(REMEMBER_ME_KEY, userDetailsService);
		return tokenBasedRememberMeServices;
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/static/**","/resources/**");
		
	}

}
