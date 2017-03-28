package com.kdev.app;

import javax.annotation.Resource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
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
}
