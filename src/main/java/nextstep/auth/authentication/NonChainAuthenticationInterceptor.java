package nextstep.auth.authentication;

import nextstep.auth.application.AuthenticationUserService;
import nextstep.auth.domain.LoginUser;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NonChainAuthenticationInterceptor implements HandlerInterceptor {

    private final AuthenticationUserService authenticationUserService;
    private final AuthenticationConverter authenticationConverter;

    public NonChainAuthenticationInterceptor(AuthenticationUserService authenticationUserService,
                                             AuthenticationConverter authenticationConverter) {
        this.authenticationUserService = authenticationUserService;
        this.authenticationConverter = authenticationConverter;
    }

    public abstract void afterComplete(LoginUser loginUser, HttpServletResponse response) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authentication = authenticationConverter.convert(request);

        LoginUser loginUser = authenticationUserService.loadUserByUsername(authentication.getPrincipal());

        if (loginUser == null) {
            throw new AuthenticationException();
        }

        if (!loginUser.checkPassword(authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        afterComplete(loginUser, response);

        return false;
    }

}
