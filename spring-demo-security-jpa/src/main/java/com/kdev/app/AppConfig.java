package com.kdev.app;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@Configuration
@EnableWebMvc
@EnableJpaRepositories(basePackages={"com.kdev.app"}, entityManagerFactoryRef="entityManagerFactoryBean", transactionManagerRef="transactionManager")
@ComponentScan(basePackages={"com.kdev.app"})
@PropertySource("classpath:application.properties")
public class AppConfig {

}
