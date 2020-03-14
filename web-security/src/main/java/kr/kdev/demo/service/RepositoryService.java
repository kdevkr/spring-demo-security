package kr.kdev.demo.service;

import kr.kdev.demo.bean.Repository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class RepositoryService extends AbstractService {

    /**
     * 신규 리파지토리 생성
     * 인증된 사용자가 REPOSITORY_CREATE 권한을 가져야만 접근할 수 있도록 제한합니다.
     */
    @PreAuthorize("hasAuthority('REPOSITORY_CREATE')")
    public String create(Repository repository) {
        // TODO : Implementation
        LOG.info("Call create method successfully!");
        return UUID.randomUUID().toString();
    }

    /**
     * 리파지토리 수정
     * {@link Repository}의 소유자와 인증 {@link java.security.Principal}에 포함된 사용자 아이디를 비교하여
     * 리파지토리 정보를 수정할 수 있는 소유자인지 확인하여 접근 제어를 수행합니다.
     */
    @PreAuthorize("@repositoryChecker.owner(authentication, #repository.id)")
    public boolean update(@P("repository") Repository repository) {
        // TODO : Implementation
        LOG.info("Call update method successfully!");
        return true;
    }

    @PreAuthorize("@repositoryChecker.ownerWithName(authentication, #id, #name)")
    public boolean delete(String id, String name) {
        LOG.info("Call delete method successfully!");
        return true;
    }
}
