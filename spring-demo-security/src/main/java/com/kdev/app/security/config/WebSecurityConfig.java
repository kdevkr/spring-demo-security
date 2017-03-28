package com.kdev.app.security.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.inMemoryAuthentication().withUser("admin").password("admin").roles("ADMIN", "DBA");
		auth.inMemoryAuthentication().withUser("scott").password("tiger").roles("USER", "SETTING");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.httpBasic()
			.and()
			.authorizeRequests()
				.anyRequest().authenticated()
			.and()
			.formLogin()
				.loginPage("/login")
				.usernameParameter("email")
				.passwordParameter("password")
				.defaultSuccessUrl("/")
				.failureUrl("/login?error")
				.permitAll();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		// TODO Auto-generated method stub
		web.ignoring().antMatchers("/static/**","/resources/**");
		
	}

}
