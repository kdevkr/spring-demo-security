package kr.kdev.demo.service;

import kr.kdev.demo.config.ServiceTestConfigurer;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;

public class RepositoryServiceTests extends ServiceTestConfigurer {

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
}
