package kr.kdev.demo.config;

import kr.kdev.demo.bean.KakaoOAuth2User;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.crypto.scrypt.SCryptPasswordEncoder;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

/**
 * Security Configuration Integration
 *
 * @author kdevkr
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(12));
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @EnableWebSecurity
    @Configuration(proxyBeanMethods = false)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final String[] staticLocations;
        private final Environment environment;
        private final DataSource dataSource;
        private final PasswordEncoder passwordEncoder;

        public WebSecurityConfig(Environment environment,
                                 DataSource dataSource,
                                 PasswordEncoder passwordEncoder,
                                 ResourceProperties resourceProperties) {
            this.environment = environment;
            this.dataSource = dataSource;
            this.passwordEncoder = passwordEncoder;
            this.staticLocations = resourceProperties.getStaticLocations();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            User.UserBuilder userBuilder = User.builder();
            String password = "password";
            String encodedPassword = passwordEncoder.encode(password);

            auth.inMemoryAuthentication()
                    .passwordEncoder(passwordEncoder)
                    .withUser(userBuilder.username("system").password(encodedPassword).roles("SYSTEM"));

            auth.jdbcAuthentication()
                    .dataSource(dataSource)
                    .passwordEncoder(passwordEncoder)
                    .withDefaultSchema().withUser(userBuilder.username("admin").password(encodedPassword).roles("ADMIN"));
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(staticLocations)
                    .and()
                .debug(environment.acceptsProfiles(Profiles.of("debug")));
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .and()
                .httpBasic();

            http.csrf().disable();

            http.authorizeRequests()
                    .antMatchers("/api/login", "/api/logout").permitAll()
                    .antMatchers("/console/**").hasAnyRole("SYSTEM")
                    .antMatchers("/", "/api/**").authenticated()
                    .anyRequest().permitAll();

            http.oauth2Login(oauth2Login -> oauth2Login
                    .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                            .customUserType(KakaoOAuth2User.class, KakaoOAuth2User.PROVIDER)));
        }
    }
}
