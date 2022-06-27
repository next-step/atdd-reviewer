package nextstep.auth.ui.authentication;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.ui.convert.AuthenticationConverter;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final UserDetailService userDetailService;
    private final AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter authenticationConverter) {
        this.userDetailService = userDetailService;
        this.authenticationConverter = authenticationConverter;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        TokenRequest tokenRequest = authenticationConverter.convert(request);
        Authentication authentication = authenticate(tokenRequest);
        afterCompletion(request, response, authentication);
        return false;
    }

    public abstract void afterCompletion(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws Exception;

    private Authentication authenticate(TokenRequest tokenRequest) {
        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        UserDetail userDetail = userDetailService.loadUserByUserName(principal);

        authenticatedMember(userDetail, credentials);

        return new Authentication(principal, userDetail.getAuthorities());
    }

    private void authenticatedMember(UserDetail userDetail, String credentials) {
        if (userDetail == null) {
            throw new AuthenticationException();
        }

        if (!userDetail.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
    }
}
