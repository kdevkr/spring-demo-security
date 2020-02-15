package kr.kdev.demo.service;

import kr.kdev.demo.bean.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * invoke authentication
 *
 * @author kdevkr
 */
@Service
public class AuthenticationService extends AbstractService {
    private final AuthenticationProvider authenticationProvider;

    public AuthenticationService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public UserDetails authentication(User user) {
        return authentication(user.getId(), user.getPassword());
    }

    /**
     * 사용자의 아이디와 패스워드를 토큰 정보로 구성하여 {@link ProviderManager}로 인증 처리를 요청한 뒤
     * {@link SecurityContextHolder}로 부터 받아온 {@link SecurityContext}에 인증 정보를 적재합니다.
     */
    public UserDetails authentication(String id, String password) {
        LOG.debug("Call authentication({}, {})", id, password);
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);
        securityContext.setAuthentication(authentication);
        return (UserDetails) authentication.getPrincipal();
    }
}
