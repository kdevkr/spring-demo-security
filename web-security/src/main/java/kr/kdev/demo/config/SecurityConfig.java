package kr.kdev.demo.config;

import kr.kdev.demo.service.UserService;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
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
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.sql.DataSource;
import java.time.Duration;
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
     *
     * 다음은 {@link DelegatingPasswordEncoder}를 사용하여 PasswordEncoder 체인을 구성합니다.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        String idForEncode = "bcrypt";

        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put(idForEncode, new BCryptPasswordEncoder(12));
//        encoders.put("pbkdf2", new Pbkdf2PasswordEncoder());
//        encoders.put("scrypt", new SCryptPasswordEncoder());
        encoders.put("noop", NoOpPasswordEncoder.getInstance());

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

    @EnableWebSecurity
    @Configuration(proxyBeanMethods = false)
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter implements EnvironmentAware {

        private final String[] staticLocations;
        private final DataSource dataSource;
        private final PasswordEncoder passwordEncoder;
        private final UserService userService;
        private final DaoAuthenticationProvider daoAuthenticationProvider;

        private Environment environment;

        public WebSecurityConfig(DataSource dataSource,
                                 PasswordEncoder passwordEncoder,
                                 ResourceProperties resourceProperties,
                                 UserService userService,
                                 DaoAuthenticationProvider daoAuthenticationProvider) {
            this.dataSource = dataSource;
            this.passwordEncoder = passwordEncoder;
            this.staticLocations = resourceProperties.getStaticLocations();
            this.userService = userService;
            this.daoAuthenticationProvider = daoAuthenticationProvider;
        }

        /**
         * {@link AuthenticationManagerBuilder}를 통해 {@link AuthenticationProvider}를 추가하십시오.
         * AuthenticationManagerBuilder는 {@link ProviderManager}를 구성합니다.
         */
        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            User.UserBuilder userBuilder = User.builder();
            String password = "password";
            String encodedPassword = passwordEncoder.encode(password);

            // InMemoryUserDetailsManager는 사용자 정보를 메모리에 저장하여 인증할 수 있도록 제공합니다.
            auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder)
                .withUser(userBuilder.username("system").password(encodedPassword).roles("SYSTEM"));

            if(environment.acceptsProfiles(Profiles.of("postgres"))) {
                // DaoAuthenticationProvider는 UserDetailsService에서 제공하는 사용자 정보를 통해 인증할 수 있도록 제공합니다.
                auth.authenticationProvider(daoAuthenticationProvider)
                    .userDetailsService(userService).passwordEncoder(passwordEncoder);
            } else {
                // JdbcUserDetailsManager는 기본 JDBC 스키마를 사용해서 사용자 정보를 구성하고 인증할 수 있도록 제공합니다.
                auth.jdbcAuthentication()
                    .dataSource(dataSource).passwordEncoder(passwordEncoder).withDefaultSchema()
                        .withUser(userBuilder.username("admin").password(encodedPassword).roles("ADMIN")
                                    .authorities("REPOSITORY_CREATE"));
            }
        }

        /**
         * 웹에 대한 보안 설정
         */
        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(staticLocations);

//            StrictHttpFirewall httpFirewall = new StrictHttpFirewall();
//            httpFirewall.setAllowedHostnames((hostname)-> hostname.equals("github.com"));
//            httpFirewall.setAllowedHostnames((hostname)-> hostname.equals("localhost"));
//            web.httpFirewall(httpFirewall);

            // 웹 보안에 대한 디버그 메시지를 활성화 할 수 있습니다.
            web.debug(environment.acceptsProfiles(Profiles.of("debug")));
        }

        /**
         * HTTP에 대한 보안 설정
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .loginPage("/login");
            http.httpBasic().disable();
            http.csrf().disable();

//            http.cors(cors -> cors.configurationSource(configurationSource()));

            // Permit login and logout api.
            // but, other apis only can access authenticated user.
            http.authorizeRequests()
                    .antMatchers("/api/login", "/api/logout").permitAll()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/console/**", "/api/console/**").hasRole("SYSTEM")
                    .antMatchers("/").permitAll();
        }

        @Bean
        public AuthenticationSuccessHandler authenticationSuccessHandler() {
            SavedRequestAwareAuthenticationSuccessHandler authenticationSuccessHandler = new SavedRequestAwareAuthenticationSuccessHandler();
            authenticationSuccessHandler.setTargetUrlParameter("redirect-uri");
            return authenticationSuccessHandler;
        }

        /**
         * Cors 구성 예시
         * @return Cors 설정 정보
         */
        private CorsConfigurationSource configurationSource() {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setMaxAge(Duration.ofDays(1));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.addAllowedOrigin("*");
            corsConfiguration.addAllowedHeader("*");
            corsConfiguration.addAllowedMethod(HttpMethod.GET);

            UrlBasedCorsConfigurationSource configurationSource = new UrlBasedCorsConfigurationSource();
            configurationSource.setRemoveSemicolonContent(true);
            configurationSource.registerCorsConfiguration("/**", corsConfiguration);
            return configurationSource;
        }

        @Override
        public void setEnvironment(Environment environment) {
            this.environment = environment;
        }
    }

    /**
     * 메소드 보안 설정
     *
     *  prePostEnabled : {@link PreAuthorize}와 {@link PostAuthorize}를 사용하여 함수에 대한 접근을 제어합니다.
     *  securedEnabled : {@link Secured} 어노테이션을 사용하여 함수에 대한 접근을 제어합니다.
     *  jsr250Enabled : JSR-250 기반의 어노테이션을 사용하여 함수에 대한 접근을 제어합니다.
     */
    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = false, jsr250Enabled = false)
    @Configuration(proxyBeanMethods = false)
    public static class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    }
}
