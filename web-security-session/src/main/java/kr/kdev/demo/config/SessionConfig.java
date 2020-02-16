package kr.kdev.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.BeanClassLoaderAware;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.session.data.redis.RedisIndexedSessionRepository;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;
import org.springframework.session.security.SpringSessionBackedSessionRegistry;
import org.springframework.session.security.web.authentication.SpringSessionRememberMeServices;
import redis.embedded.RedisServer;

/**
 * Session Configuration Integration
 *
 * @author kdevkr
 */
@Configuration(proxyBeanMethods = false)
public class SessionConfig {

    private final RedisIndexedSessionRepository sessionRepository;

    public SessionConfig(RedisIndexedSessionRepository sessionRepository) {
        this.sessionRepository = sessionRepository;
    }

    @Bean
    public SpringSessionBackedSessionRegistry sessionRegistry() {
        return new SpringSessionBackedSessionRegistry<>(sessionRepository);
    }

    /**
     * Activate Remember-me service with Spring Security.
     * {@link SecurityConfig.WebSecurityConfig}
     */
    @Bean
    public SpringSessionRememberMeServices rememberMeServices() {
        SpringSessionRememberMeServices rememberMeServices = new SpringSessionRememberMeServices();
        rememberMeServices.setAlwaysRemember(true);
        return rememberMeServices;
    }

    @EnableRedisHttpSession
    @Configuration(proxyBeanMethods = false)
    public static class HttpSessionConfig implements BeanClassLoaderAware {

        private ClassLoader classLoader;

        @Bean
        public ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
            return new ServletListenerRegistrationBean<>(new HttpSessionEventPublisher());
        }

        @Bean
        public RedisSerializer<Object> redisSerializer(ObjectMapper objectMapper) {
            return new GenericJackson2JsonRedisSerializer(objectMapper.copy().registerModules(SecurityJackson2Modules.getModules(classLoader)));
        }

        @Override
        public void setBeanClassLoader(ClassLoader classLoader) {
            this.classLoader = classLoader;
        }
    }

    /**
     * Can't use embedded redis server in windows.
     */
    @Profile("embedded-redis")
    @Configuration(proxyBeanMethods = false)
    public static class EmbeddedRedisConfig {

        public EmbeddedRedisConfig() {}

        @Bean
        public RedisServerBean redisServer(RedisProperties redisProperties) {
            return new RedisServerBean(redisProperties.getPort());
        }

        class RedisServerBean implements InitializingBean, DisposableBean {
            private RedisServer redisServer;
            private int port;

            public RedisServerBean(int port) {
                this.port = port;
            }

            public void afterPropertiesSet() {
                redisServer = new RedisServer(port);
                redisServer.start();
            }

            public void destroy() {
                if (redisServer != null) {
                    redisServer.stop();
                }
            }
        }
    }

}
