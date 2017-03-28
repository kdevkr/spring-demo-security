package com.kdev.app;

import java.util.Locale;

import javax.annotation.Resource;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

@Configuration
public class WebServletConfig extends WebMvcConfigurerAdapter {
	
	@Resource
	private Environment environment;

	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		// TODO Auto-generated method stub
		registry.addResourceHandler("/static/**").addResourceLocations("/static");
		registry.addResourceHandler("/resources/**").addResourceLocations("/resources");
	}
	
	@Bean
	public InternalResourceViewResolver jstlViewResolver(){
		InternalResourceViewResolver jstlViewResolver = new InternalResourceViewResolver();
		jstlViewResolver.setOrder(1);
		jstlViewResolver.setViewClass(JstlView.class);
		jstlViewResolver.setPrefix(environment.getProperty("spring.mvc.view.prefix"));
		jstlViewResolver.setSuffix(environment.getProperty("spring.mvc.view.suffix"));
		return jstlViewResolver;
	}
	
	/*
	 * Spring Security Localization
	 */
	
	@Bean
	public MessageSource messageSource(){
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.addBasenames("security/messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}
	
	@Bean
	public LocaleChangeInterceptor localeChangeInterceptor(){
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		return localeChangeInterceptor;
	}
	
	@Bean
	public LocaleResolver localeResolver(){
		SessionLocaleResolver localeResolver = new SessionLocaleResolver();
		localeResolver.setDefaultLocale(Locale.KOREAN);
		return localeResolver;
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// TODO Auto-generated method stub
		registry.addInterceptor(localeChangeInterceptor());
	}
}
