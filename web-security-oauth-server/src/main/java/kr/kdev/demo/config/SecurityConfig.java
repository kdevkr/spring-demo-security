package kr.kdev.demo.config;

import kr.kdev.demo.service.ClientService;
import kr.kdev.demo.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.core.env.Profiles;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AuthenticationEntryPoint;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.web.firewall.DefaultHttpFirewall;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.header.writers.ReferrerPolicyHeaderWriter;

/**
 * Security Configuration Integration
 *
 * @author kdevkr
 */
@Configuration(proxyBeanMethods = false)
public class SecurityConfig {

    public static final int PRINCIPAL_LOCK_BASELINE = 5;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);
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
    public static class WebSecurityConfig extends WebSecurityConfigurerAdapter {

        private final String[] staticLocations;
        private final Environment environment;
        private final PasswordEncoder passwordEncoder;
        private final UserService userService;
        private final SessionRegistry sessionRegistry;


        public WebSecurityConfig(Environment environment,
                                 PasswordEncoder passwordEncoder,
                                 ResourceProperties resourceProperties,
                                 UserService userService,
                                 SessionRegistry sessionRegistry) {
            this.environment = environment;
            this.passwordEncoder = passwordEncoder;
            this.staticLocations = resourceProperties.getStaticLocations();
            this.userService = userService;
            this.sessionRegistry = sessionRegistry;
        }

        @Bean
        public HttpFirewall httpFirewall() {
            return new DefaultHttpFirewall();
        }

        @Bean
        @Override
        public AuthenticationManager authenticationManagerBean() throws Exception {
            return super.authenticationManagerBean();
        }

        @Autowired
        public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
            auth.userDetailsService(userService).passwordEncoder(passwordEncoder);
        }

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth
                .userDetailsService(userService)
                .passwordEncoder(passwordEncoder);
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring().antMatchers(staticLocations)
                    .and()
                .debug(environment.acceptsProfiles(Profiles.of("debug")));
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.formLogin()
                    .and()
                .httpBasic();

            http.csrf().disable();

            http.authorizeRequests()
                    .antMatchers("/api/**").authenticated()
                    .antMatchers("/oauth/authorize").authenticated()
                    .anyRequest().permitAll();

            http.sessionManagement().maximumSessions(5).sessionRegistry(sessionRegistry);

            http.headers(headers ->
                    headers.referrerPolicy(referrerPolicy ->
                            referrerPolicy.policy(ReferrerPolicyHeaderWriter.ReferrerPolicy.SAME_ORIGIN)));
        }
    }

    @EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = false)
    @Configuration(proxyBeanMethods = false)
    public static class GlobalMethodSecurityConfig extends GlobalMethodSecurityConfiguration {

    }

    @EnableAuthorizationServer
    @Configuration
    public static class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

        private final PasswordEncoder passwordEncoder;
        private final ClientService clientService;
        private final AuthenticationManager authenticationManager;

        public AuthorizationServerConfig(PasswordEncoder passwordEncoder,
                                         ClientService clientService,
                                         AuthenticationManager authenticationManager) {
            this.passwordEncoder = passwordEncoder;
            this.clientService = clientService;
            this.authenticationManager = authenticationManager;
        }

        /**
         * JWTs can be signed using a secret (with the HMAC algorithm) or a public/private key pair using RSA or ECDSA.
         */
        @Bean
        public JwtAccessTokenConverter accessTokenConverter() {
            JwtAccessTokenConverter accessTokenConverter = new JwtAccessTokenConverter();
            accessTokenConverter.setSigningKey("password");
//            accessTokenConverter.setKeyPair();
            return accessTokenConverter;
        }

        @Bean
        public TokenStore tokenStore() {
            return new JwtTokenStore(accessTokenConverter());
        }

        @Bean
        @Primary
        public DefaultTokenServices tokenServices() {
            DefaultTokenServices tokenServices = new DefaultTokenServices();
            tokenServices.setTokenStore(tokenStore());
            tokenServices.setClientDetailsService(clientService);
            tokenServices.setSupportRefreshToken(true);
            return tokenServices;
        }

        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) {
            security
                .allowFormAuthenticationForClients()
                .authenticationEntryPoint(new OAuth2AuthenticationEntryPoint())
                .passwordEncoder(passwordEncoder);
        }

        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(clientService);
        }

        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) {
            endpoints
                .authenticationManager(authenticationManager)
                .accessTokenConverter(accessTokenConverter())
                .tokenStore(tokenStore());
        }
    }

    @Configuration
    @EnableResourceServer
    public static class ResourceServerConfig implements ResourceServerConfigurer {

        @Override
        public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
            resources.authenticationEntryPoint(new OAuth2AuthenticationEntryPoint());
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.requestMatchers()
                    .antMatchers("/api/v1/**")
                .and()
                    .authorizeRequests()
                    .anyRequest().authenticated();
        }
    }

}
