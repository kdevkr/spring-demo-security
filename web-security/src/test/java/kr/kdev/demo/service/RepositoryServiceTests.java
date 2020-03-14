package kr.kdev.demo.service;

import kr.kdev.demo.bean.Repository;
import kr.kdev.demo.config.ServiceTestConfigurer;
import kr.kdev.demo.expression.RepositoryChecker;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;

import java.util.UUID;

@ComponentScan(basePackageClasses = {RepositoryChecker.class})
public class RepositoryServiceTests extends ServiceTestConfigurer {

    @Autowired
    private RepositoryService repositoryService;

    @WithMockUser(authorities = {"REPOSITORY_CREATE"})
    @Test
    public void TEST_000_createRepository() {
        Repository repository = new Repository();
        String repositoryId = repositoryService.create(repository);
        Assert.assertNotNull(repositoryId);
        LOG.info("repositoryId : {}", repositoryId);
    }

    @WithMockUser(authorities = {})
    @Test(expected = AccessDeniedException.class)
    public void TEST_000_createRepositoryNoneAuthority() {
        Repository repository = new Repository();
        String repositoryId = repositoryService.create(repository);
        Assert.assertNotNull(repositoryId);
    }

    @WithUserDetails(userDetailsServiceBeanName = "userService")
    @Test
    public void TEST_001_updateRepository() {
        Repository repository = new Repository();
        repository.setId(UUID.randomUUID().toString());
        boolean updated = repositoryService.update(repository);
        LOG.info("updated : {}", updated);
    }

    @WithUserDetails(userDetailsServiceBeanName = "userService")
    @Test(expected = AccessDeniedException.class)
    public void TEST_001_updateRepositoryWithIdIsNull() {
        Repository repository = new Repository();
        boolean updated = repositoryService.update(repository);
        LOG.info("updated : {}", updated);
    }

    @WithUserDetails(userDetailsServiceBeanName = "userService")
    @Test(expected = AccessDeniedException.class)
    public void TEST_002_deleteRepositoryWithNameIsNull() {
        boolean deleted = repositoryService.delete("dummy", null);
        LOG.info("deleted : {}", deleted);
    }
}
