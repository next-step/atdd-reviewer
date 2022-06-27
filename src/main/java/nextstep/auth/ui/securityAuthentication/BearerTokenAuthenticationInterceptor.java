package nextstep.auth.ui.securityAuthentication;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class BearerTokenAuthenticationInterceptor extends SecurityAuthenticationInterceptor {
    private final JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationInterceptor(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
            AuthenticationToken token = new AuthenticationToken(authCredentials, authCredentials);

            if (!jwtTokenProvider.validateToken(token.getPrincipal())) {
                throw new AuthenticationException();
            }

            String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
            List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

            Authentication authentication = new Authentication(principal, roles);

            afterCompletion(request, response, authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
