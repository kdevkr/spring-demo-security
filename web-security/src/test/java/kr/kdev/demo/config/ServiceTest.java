package kr.kdev.demo.config;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.jdbc.TestDatabaseAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;

import java.lang.annotation.*;

/**
 * Apply test environment for Service Layer based on {@link JdbcTest}
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@JdbcTest(excludeAutoConfiguration = TestDatabaseAutoConfiguration.class)
@ImportAutoConfiguration(classes = {
        SecurityAutoConfiguration.class
})
@EnableConfigurationProperties({ ResourceProperties.class })
@ComponentScan(basePackages = {
        "kr.kdev.demo.service",
        "kr.kdev.demo.repository"
})
@ContextConfiguration(classes = {
        SecurityConfig.class
})
public @interface ServiceTest {
}
