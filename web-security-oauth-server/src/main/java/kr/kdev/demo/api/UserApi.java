package kr.kdev.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.OAuth2Request;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

@RestController
public class UserApi extends BaseApi {

    private final SessionRegistry sessionRegistry;

    public UserApi(SessionRegistry sessionRegistry) {
        this.sessionRegistry = sessionRegistry;
    }

    @GetMapping("/users/me")
    public ResponseEntity<Object> currentUser(@AuthenticationPrincipal UserDetails principal,
                                              // same As SecurityContextHolder.getContext().getAuthentication()
                                              @CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            if(authentication instanceof OAuth2Authentication) {
                OAuth2Request oAuth2Request = ((OAuth2Authentication) authentication).getOAuth2Request();
            } else {
                principal = (UserDetails) authentication.getPrincipal();
            }
        }
        return ResponseEntity.ok(principal);
    }

    @GetMapping("/users")
    public ResponseEntity<Object> allUsers() {
        List<Object> allPrincipals = sessionRegistry.getAllPrincipals();
        return ResponseEntity.ok(allPrincipals);
    }

    @GetMapping("/users/me/sessions")
    public ResponseEntity<Object> currentUserSessions(@CurrentSecurityContext(expression = "authentication") Authentication authentication,
                                                      @RequestParam(required = false) boolean includeExpiredSessions) {
        List<SessionInformation> allSessions = sessionRegistry.getAllSessions(authentication.getPrincipal(), includeExpiredSessions);
        return ResponseEntity.ok(allSessions);
    }

    @GetMapping("/users/me/sessionInformation")
    public ResponseEntity<Object> currentUserSessionInformation(@CurrentSecurityContext(expression = "authentication") Authentication authentication) {
        String sessionId = RequestContextHolder.currentRequestAttributes().getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        return ResponseEntity.ok(sessionInformation);
    }

    @GetMapping("/users/me/sessionInformation2")
    public ResponseEntity<Object> currentUserSessionInformation2(@CurrentSecurityContext(expression = "authentication.details") WebAuthenticationDetails details) {
        String sessionId = details.getSessionId();
        SessionInformation sessionInformation = sessionRegistry.getSessionInformation(sessionId);
        return ResponseEntity.ok(sessionInformation);
    }

}
