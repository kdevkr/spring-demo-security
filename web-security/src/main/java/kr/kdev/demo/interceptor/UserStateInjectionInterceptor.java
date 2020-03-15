package kr.kdev.demo.interceptor;

import kr.kdev.demo.bean.User;
import kr.kdev.demo.bean.UserState;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class UserStateInjectionInterceptor implements HandlerInterceptor {

    private final Logger LOG = LoggerFactory.getLogger(UserStateInjectionInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        if(modelAndView != null) {
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            LOG.info("Injection UserState from authentication to modelAndView");

            UserState userState = new UserState();
            if(authentication != null) {

                if(authentication instanceof UsernamePasswordAuthenticationToken) {
                    Object principal = authentication.getPrincipal();
                    if(principal instanceof User) {
                        User user = (User) principal;
                        userState.setUserId(user.getUserId());
                        userState.setName(user.getName());
                    } else {
                        UserDetails userDetails = (UserDetails) principal;
                        userState.setUserId(UUID.randomUUID().toString());
                        userState.setName(userDetails.getUsername());
                    }
                }

            }

            userState.setSessionId(sessionId);
            modelAndView.addObject("state", userState);
        }
    }
}
