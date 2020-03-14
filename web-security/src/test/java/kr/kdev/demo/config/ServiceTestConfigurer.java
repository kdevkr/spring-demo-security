package kr.kdev.demo.config;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles({"test"})
@RunWith(SpringJUnit4ClassRunner.class)
@ServiceTest
public class ServiceTestConfigurer {

    protected Logger LOG = LoggerFactory.getLogger(getClass());
    protected SecurityContext securityContext;

    @Before
    public void setUp() {
        this.securityContext = SecurityContextHolder.getContext();
    }
}
