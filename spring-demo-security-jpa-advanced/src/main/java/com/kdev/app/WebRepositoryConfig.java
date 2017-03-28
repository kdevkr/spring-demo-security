package com.kdev.app;

import java.sql.SQLException;
import java.util.Properties;

import javax.annotation.Resource;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
public class WebRepositoryConfig {
	
	@Resource
	private Environment environment;
	
    @Bean
    public DataSource dataSource() {
    	EmbeddedDatabaseBuilder builder = new EmbeddedDatabaseBuilder();
    	EmbeddedDatabase embeddedDatabase = builder.setType(EmbeddedDatabaseType.H2)
    			.build();
    	
        /*DataSource dataSource = new org.apache.tomcat.jdbc.pool.DataSource();
        dataSource.setDriverClassName(environment.getProperty("spring.datasource.jdbc.driverClassName"));
        dataSource.setUsername(environment.getProperty("spring.datasource.jdbc.username"));
        dataSource.setUrl(environment.getProperty("spring.datasource.jdbc.url"));
        dataSource.setPassword(environment.getProperty("spring.datasource.jdbc.password"));
        dataSource.setConnectionProperties("true");
        dataSource.setTestOnReturn(true);
        dataSource.setTestWhileIdle(true);
        dataSource.setRemoveAbandoned(true);
        dataSource.setRemoveAbandonedTimeout(30);
        dataSource.setLogAbandoned(true);
        dataSource.setValidationQuery("SELECT 1");
    	
        return dataSource;*/
    	return embeddedDatabase;
    }
    
    @Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactoryBean(){
		LocalContainerEntityManagerFactoryBean entityManagerFactoryBean = new LocalContainerEntityManagerFactoryBean();
		entityManagerFactoryBean.setDataSource(dataSource());
		entityManagerFactoryBean.setPackagesToScan(new String[]{"com.kdev.app"});
		
		JpaVendorAdapter jpaVendorAdapter = new HibernateJpaVendorAdapter();
		entityManagerFactoryBean.setJpaVendorAdapter(jpaVendorAdapter);
		
		Properties properties = new Properties();
	    properties.setProperty("hibernate.hbm2ddl.auto", environment.getProperty("spring.hibernate.hbm2ddl.auto"));
	    properties.setProperty("hibernate.dialect", environment.getProperty("spring.hibernate.dialect"));
	    properties.setProperty("hiberante.ejb.naming_strategy", environment.getProperty("spring.hiberante.ejb.naming_strategy"));
	    properties.setProperty("hibernate.show_sql", environment.getProperty("spring.hibernate.show_sql"));
	    entityManagerFactoryBean.setJpaProperties(properties);
		return entityManagerFactoryBean;
	}
    
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory entityManagerFactory){
    	JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
    	jpaTransactionManager.setEntityManagerFactory(entityManagerFactory);
    	return jpaTransactionManager;
    }
    
    // http://kwonnam.pe.kr/wiki/springframework/jpa
    @Bean
    public PersistenceExceptionTranslationPostProcessor exceptionTranslation(){
       return new PersistenceExceptionTranslationPostProcessor();
    }
    
    @Bean(initMethod="start",destroyMethod="stop")
    public org.h2.tools.Server h2WebConsonleServer () throws SQLException {
      return org.h2.tools.Server.createWebServer("-web","-webAllowOthers","-webDaemon","-webPort", "8082");
    }
}
