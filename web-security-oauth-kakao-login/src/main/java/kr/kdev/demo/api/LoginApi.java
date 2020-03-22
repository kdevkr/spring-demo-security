package kr.kdev.demo.api;

import kr.kdev.demo.bean.User;
import kr.kdev.demo.service.AuthenticationService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

@RestController
public class LoginApi extends BaseApi {

    private final AuthenticationService authenticationService;

    public LoginApi(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<Object> login(@ModelAttribute User user) {
        user.setId(user.getUsername());
        User principal = (User) authenticationService.authentication(user);
        return ResponseEntity.ok(principal);
    }

    @GetMapping("logout")
    public ResponseEntity<Object> logout(HttpServletRequest request) throws ServletException {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if(authentication != null) {
            request.logout();
        }

        return ResponseEntity.ok(true);
    }
}
