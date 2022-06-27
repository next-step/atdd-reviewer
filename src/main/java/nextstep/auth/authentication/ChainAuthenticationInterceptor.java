package nextstep.auth.authentication;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class ChainAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationConverter authenticationConverter;

    public ChainAuthenticationInterceptor(AuthenticationConverter authenticationConverter) {
        this.authenticationConverter = authenticationConverter;
    }

    public abstract void afterAuthentication(AuthenticationToken token) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            AuthenticationToken token = authenticationConverter.convert(request);
            afterAuthentication(token);
            return true;
        } catch (Exception e) {
            return true;
        }
    }

}
