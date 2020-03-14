package kr.kdev.demo.expression;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class RepositoryChecker {
    private static final Logger LOG = LoggerFactory.getLogger(RepositoryChecker.class);

    @PreAuthorize("isAuthenticated()")
    public boolean owner(Authentication authentication, String id) {
        // TODO : 인증된 사용자가 ownerId와 같은지를 검증합니다.
        if(id == null) {
            LOG.error("id is null");
            return false;
        }
        return true;
    }

    @PreAuthorize("isAuthenticated()")
    public boolean ownerWithName(Authentication authentication, String id, String name) {
        // TODO : 인증된 사용자가 ownerId와 같은지 리파지토리 이름이 같은지 검증합니다.
        if(id == null || name == null) {
            LOG.error("id or name is null");
            return false;
        }
        return true;
    }
}
