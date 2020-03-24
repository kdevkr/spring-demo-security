package kr.kdev.demo.service;

import kr.kdev.demo.bean.User;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService extends AbstractService {
    private final AuthenticationProvider authenticationProvider;

    public AuthenticationService(AuthenticationProvider authenticationProvider) {
        this.authenticationProvider = authenticationProvider;
    }

    public UserDetails authentication(User user) {
        return authentication(user.getId(), user.getPassword());
    }

    public UserDetails authentication(String id, String password) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(id, password);
        Authentication authentication = authenticationProvider.authenticate(authenticationToken);
        securityContext.setAuthentication(authentication);
        return (UserDetails) authentication.getPrincipal();
    }
}
