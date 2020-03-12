package kr.kdev.demo.config;

import kr.kdev.demo.service.UserService;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
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
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

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

    public static final int PRINCIPAL_LOCK_BASELINE = 5;

    /**
     * Spring Boot 2.0는 기본적으로 BCryptPasswordEncoder를 사용합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(12));
        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
        encoders.put("scrypt", new SCryptPasswordEncoder());

        return new DelegatingPasswordEncoder(idForEncode, encoders);
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(PasswordEncoder passwordEncoder,
                                                               UserService userService) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setUserDetailsService(userService);
        return authenticationProvider;
    }

    /**
     * 웹 보안 설정
     * WebSecurityConfigurerAdapter를 확장하여 보안 설정을 커스터마이징할 수 있습니다.
     */
    @EnableWebSecurity
    @Configuration(proxyBeanMethods = false)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final String[] staticLocations;
        private final Environment environment;
        private final DataSource dataSource;
        private final PasswordEncoder passwordEncoder;
        private final UserService userService;
        private final DaoAuthenticationProvider daoAuthenticationProvider;

        public WebSecurityConfig(Environment environment,
                                 DataSource dataSource,
                                 PasswordEncoder passwordEncoder,
                                 ResourceProperties resourceProperties,
                                 UserService userService,
                                 DaoAuthenticationProvider daoAuthenticationProvider) {
            this.environment = environment;
            this.dataSource = dataSource;
            this.passwordEncoder = passwordEncoder;
            this.staticLocations = resourceProperties.getStaticLocations();
            this.userService = userService;
            this.daoAuthenticationProvider = daoAuthenticationProvider;
        }

        /**
         * ProviderManager 커스터마이징
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            User.UserBuilder userBuilder = User.builder();
            String password = "{bcrypt}$2a$12$ry/T4SyQyiNpaWbadf9sne3Cko..q92Oh2klkCMv4XB1qG6cy8iaG";

            // InMemoryUserDetailsManager는 자체적으로 사용자 정보를 메모리에 보유하고 이를 활용하여 인증합니다.
            auth.inMemoryAuthentication().passwordEncoder(passwordEncoder).withUser(userBuilder.username("system").password(password).roles("SYSTEM"));


            // JdbcDaoImpl를 UserDetailsService로 지정하여 스프링 시큐리티가 제공하는 기본 DB 스키마를 사용합니다.
            auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder).withDefaultSchema().withUser(userBuilder.username("admin").password(password).roles("ADMIN"));


            // JdbcDaoImpl 대신에 애플리케이션 사용자 스키마를 적용하기 위하여 개별적으로 구현된 UserDetailsService를 사용합니다.
            // 이는 DaoAuthenticationProvider를 authenticationProvider로 지정하는 것과 동일합니다.
            auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
            auth.authenticationProvider(daoAuthenticationProvider);
        }

        /**
         * 웹 보안 커스터마이징
         */
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(staticLocations)
                    .and()
                .debug(environment.acceptsProfiles(Profiles.of("debug")));
        }

        /**
         * HTTP 보안 커스터마이징
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .and()
                .httpBasic();

            http.csrf().disable();

            http.authorizeRequests()
                    .antMatchers("/api/login", "/api/logout").permitAll()
                    .antMatchers("/console/**").hasAnyRole("SYSTEM")
                    .antMatchers("/api/**").authenticated()
                    .anyRequest().permitAll();

            http.formLogin().successHandler(authenticationSuccessHandler());
        }

        @Bean
        public AuthenticationSuccessHandler authenticationSuccessHandler() {
            SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            authenticationSuccessHandler.setTargetUrlParameter("redirect-uri");
            return authenticationSuccessHandler;
        }
    }

    /**
     * 메소드 기반 보안 설정
     *
     *  prePostEnabled : {@link PreAuthorize}와 {@link PostAuthorize}를 사용하여 함수에 대한 접근을 제어합니다.
     *  securedEnabled : {@link Secured} 어노테이션을 사용하여 함수에 대한 접근을 제어합니다.
     *  jsr250Enabled : JSR-250 기반의 어노테이션을 사용하여 함수에 대한 접근을 제어합니다.
     */
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = false)
    @Configuration(proxyBeanMethods = false)
    public static class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    }
}
