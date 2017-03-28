package com.kdev.app;

import javax.servlet.Filter;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.filter.HiddenHttpMethodFilter;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.kdev.app.security.config.WebSecurityConfig;

public class ApplicationInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

	@Override
	protected Class<?>[] getRootConfigClasses() {
		// TODO Auto-generated method stub
		return new Class<?>[]{AppConfig.class, WebSecurityConfig.class};
	}

	@Override
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return new Class<?>[]{WebServletConfig.class};
	}

	@Override
	protected String[] getServletMappings() {
		// TODO Auto-generated method stub
		return new String[]{"/"};
	}

	@Override
	protected Filter[] getServletFilters() {
		// TODO Auto-generated method stub
		return new Filter[]{new CharacterEncodingFilter("UTF-8"), new HiddenHttpMethodFilter()};
	}

	@Override
	protected void registerDispatcherServlet(ServletContext servletContext) {
		// TODO Auto-generated method stub
		WebApplicationContext webApplicationContext = createServletApplicationContext();
		DispatcherServlet dispatherServlet = new DispatcherServlet(webApplicationContext);
		ServletRegistration.Dynamic appServlet = servletContext.addServlet("appServlet", dispatherServlet);
		appServlet.setLoadOnStartup(1);
		appServlet.addMapping(getServletMappings());
	}
	
	

}
