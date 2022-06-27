package nextstep.auth.ui.securityAuthentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class SecurityAuthenticationInterceptor implements HandlerInterceptor {

    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
