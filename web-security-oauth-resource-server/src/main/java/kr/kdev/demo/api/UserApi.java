package kr.kdev.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/users")
public class UserApi {

    public UserApi() {
    }

    @GetMapping("/me")
    public ResponseEntity<Object> currentUser(@CurrentSecurityContext(expression = "authentication") OAuth2Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }

}
