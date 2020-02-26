package kr.kdev.demo.handler;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * this handler called after {@link LogoutFilter}
 *
 * More details https://docs.spring.io/spring-security/site/docs/5.2.2.RELEASE/reference/html5/#jc-logout-success-handler
 */
@Component
public class LogoutHistoryHandler extends HttpStatusReturningLogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        // TODO : save logout log.

        super.onLogoutSuccess(request, response, authentication);
    }
}
