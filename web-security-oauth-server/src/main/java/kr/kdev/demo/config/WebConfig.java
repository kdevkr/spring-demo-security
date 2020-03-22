package kr.kdev.demo.config;

import kr.kdev.demo.interceptor.PrincipalInjectionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration(proxyBeanMethods = false)
public class WebConfig {

    @Configuration
    public static class WebMvcConfig implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(new PrincipalInjectionInterceptor());
        }
    }
}
