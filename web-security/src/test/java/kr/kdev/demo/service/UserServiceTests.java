package kr.kdev.demo.service;

import kr.kdev.demo.config.ServiceTestConfigurer;
import kr.kdev.demo.context.WithMockAdmin;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.Collection;

public class UserServiceTests extends ServiceTestConfigurer {

    /**
     * 익명 사용자 테스트
     */
    @WithAnonymousUser
    @Test
    public void TEST_000_withAnonymousUser() {
        // Expect current thread in security context...
        SecurityContext context = SecurityContextHolder.getContext();
        Assert.assertNotNull(context);

        // Is exist authentication in security context?
        Authentication authentication = context.getAuthentication();
        Assert.assertNotNull(authentication);

        // Is AnonymousAuthenticationToken?
        Assert.assertTrue(authentication instanceof AnonymousAuthenticationToken);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    public void TEST_001_withMockUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Assert.assertNotNull(context);

        Authentication authentication = context.getAuthentication();
        Assert.assertNotNull(authentication);

        Assert.assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);

        // Is contains "ADMIN" role
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Assert.assertTrue(authorities.contains(new SimpleGrantedAuthority("ROLE_ADMIN")));

        // Is equals org.springframework.security.core.userdetails.User?
        Object principal = authentication.getPrincipal();
        Assert.assertEquals(principal.getClass(), org.springframework.security.core.userdetails.User.class);

        org.springframework.security.core.userdetails.User user = (org.springframework.security.core.userdetails.User) principal;
        LOG.info("user : {}", user);
    }

    @WithUserDetails(userDetailsServiceBeanName = "userService")
    @Test
    public void TEST_003_withUserDetails() {
        SecurityContext context = SecurityContextHolder.getContext();
        Assert.assertNotNull(context);

        Authentication authentication = context.getAuthentication();
        Assert.assertNotNull(authentication);

        Assert.assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);

        // Is equals kr.kdev.User?
        Object principal = authentication.getPrincipal();
        Assert.assertEquals(principal.getClass(), kr.kdev.demo.bean.User.class);

        kr.kdev.demo.bean.User user = (kr.kdev.demo.bean.User) principal;
        LOG.info("user : {}", user);
        LOG.info("credentials : {}", authentication.getCredentials());
    }

    /**
     * Use test meta annotation.
     */
    @WithMockAdmin
    @Test
    public void TEST_004_withCustomMockUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Assert.assertNotNull(context);

        Authentication authentication = context.getAuthentication();
        Assert.assertNotNull(authentication);

        Assert.assertTrue(authentication instanceof UsernamePasswordAuthenticationToken);
        LOG.info("principal : {}", authentication.getPrincipal());
        LOG.info("authorities : {}", authentication.getAuthorities());
    }
}
