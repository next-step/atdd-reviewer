package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class ChainAuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            handle(request, response);
        } catch (Exception e) {
            return true;
        }
        return true;
    }

    protected abstract void handle(HttpServletRequest request, HttpServletResponse response);
}
