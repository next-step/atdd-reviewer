package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

public final class BearerAuthenticationConverter implements AuthenticationConverter {

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BEARER);
        AuthenticationToken token = new AuthenticationToken(authCredentials, authCredentials);
        return token;
    }

}