package nextstep.auth.authentication;

import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;

public class BearerTokenAuthenticationFilter2 extends ChainAuthenticationFilter {
    private JwtTokenProvider jwtTokenProvider;

    public BearerTokenAuthenticationFilter2(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        AuthenticationToken token = new AuthenticationToken(authCredentials, authCredentials);

        if (!jwtTokenProvider.validateToken(token.getPrincipal())) {
            throw new AuthenticationException();
        }

        String principal = jwtTokenProvider.getPrincipal(token.getPrincipal());
        List<String> roles = jwtTokenProvider.getRoles(token.getPrincipal());

        Authentication authentication = new Authentication(principal, roles);

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
