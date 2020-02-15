package kr.kdev.demo.api;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserApi extends BaseApi {

    @GetMapping("/users/me")
    public ResponseEntity<Object> currentUser(@AuthenticationPrincipal UserDetails principal) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication != null) {
            principal = (UserDetails) authentication.getPrincipal();
        }

        return ResponseEntity.ok(principal);
    }
}
