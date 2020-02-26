package kr.kdev.demo.api;

import kr.kdev.demo.bean.CurrentUser;
import kr.kdev.demo.bean.User;
import kr.kdev.demo.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Collection;

@RestController
public class LoginApi extends BaseApi {

    private final AuthenticationService authenticationService;
    private final SessionRegistry sessionRegistry;

    public LoginApi(AuthenticationService authenticationService,
                    SessionRegistry sessionRegistry) {
        this.authenticationService = authenticationService;
        this.sessionRegistry = sessionRegistry;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@ModelAttribute User user) {
        user.setId(user.getUsername());
        User principal = (User) authenticationService.authentication(user);
        return ResponseEntity.ok(principal);
    }

    @PostMapping("renewal")
    public ResponseEntity<Object> renewal(@AuthenticationPrincipal User user,
                                          @CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                          @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {

        Collection<? extends GrantedAuthority> authorities = user.getAuthorities();
        return ResponseEntity.ok(principal);
    }

    @PostMapping("expire")
    public ResponseEntity<Object> expire(@CurrentUser User user,
                                          @CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                          @CurrentSecurityContext(expression = "authentication.principal") Principal principal) {

        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);

        return ResponseEntity.ok(principal);
    }

    @GetMapping("logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) throws ServletException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
//        if(authentication != null) {
//            request.logout();
//        }
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        if(sessionInformation != null) {
            sessionInformation.expireNow();
            sessionRegistry.removeSessionInformation(sessionId);
        }

        return ResponseEntity.ok(true);
    }
}
