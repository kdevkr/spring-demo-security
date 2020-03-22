package kr.kdev.demo.interceptor;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PrincipalInjectionInterceptor implements HandlerInterceptor {
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        if(modelAndView != null) {
            String sessionId = RequestContextHolder.getRequestAttributes().getSessionId();
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            modelAndView.addObject("authentication", authentication);
            modelAndView.addObject("sessionId", sessionId);
        }
    }
}
