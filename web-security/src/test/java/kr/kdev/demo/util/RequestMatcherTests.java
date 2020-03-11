package kr.kdev.demo.util;

import org.junit.Assert;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpMethod;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.util.UrlPathHelper;

public class RequestMatcherTests {

    private static final Logger LOG = LoggerFactory.getLogger(RequestMatcherTests.class);

    static {
    }

    @Test
    public void TEST_000_validRequestByAntPathMatcher() {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**", null, true, new UrlPathHelper());
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(HttpMethod.GET.name(), "/api/users");

        boolean matches = requestMatcher.matches(httpServletRequest);
        LOG.debug("{}", matches);
        Assert.assertTrue(matches);
    }

    @Test
    public void TEST_001_invalidRequestMethodByAntPathMatcher() {
        AntPathRequestMatcher requestMatcher = new AntPathRequestMatcher("/api/**", HttpMethod.GET.name(), true, new UrlPathHelper());
        MockHttpServletRequest httpServletRequest = new MockHttpServletRequest(HttpMethod.POST.name(), "/api/users");

        boolean matches = requestMatcher.matches(httpServletRequest);
        LOG.debug("{}", matches);
        Assert.assertTrue(matches);
    }

    @Test
    public void doSomething() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Object principal = authentication.getPrincipal();
    }
}
