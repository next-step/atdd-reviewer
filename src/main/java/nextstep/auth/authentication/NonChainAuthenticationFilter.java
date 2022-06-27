package nextstep.auth.authentication;

import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.servlet.HandlerInterceptor;

public abstract class NonChainAuthenticationFilter implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            handle(request, response);
        } catch (Exception e) {
            return true;
        }
        return false;
    }

    protected abstract void handle(HttpServletRequest request, HttpServletResponse response) throws IOException;
}
