package nextstep.auth.ui.authentication;

import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.TokenRequest;
import nextstep.auth.ui.convert.AuthenticationConverter;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class AuthenticationInterceptor implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;
    private final AuthenticationConverter authenticationConverter;

    public AuthenticationInterceptor(LoginMemberService loginMemberService, AuthenticationConverter authenticationConverter) {
        this.loginMemberService = loginMemberService;
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

        LoginMember loginMember = loginMemberService.loadUserByUsername(principal);

        authenticatedMember(loginMember, credentials);

        return new Authentication(principal, loginMember.getAuthorities());
    }

    private void authenticatedMember(LoginMember loginMember, String credentials) {
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }
    }
}
