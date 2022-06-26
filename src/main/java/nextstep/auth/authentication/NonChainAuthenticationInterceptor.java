package nextstep.auth.authentication;

import nextstep.auth.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public abstract class NonChainAuthenticationInterceptor implements HandlerInterceptor {

    private final LoginMemberService loginMemberService;
    private final AuthenticationConverter authenticationConverter;

    public NonChainAuthenticationInterceptor(LoginMemberService loginMemberService,
                                             AuthenticationConverter authenticationConverter) {
        this.loginMemberService = loginMemberService;
        this.authenticationConverter = authenticationConverter;
    }

    public abstract void afterAuthentication(LoginMember loginMember, HttpServletResponse response) throws Exception;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        AuthenticationToken authentication = authenticationConverter.convert(request);

        LoginMember loginMember = loginMemberService.findByUsername(authentication.getPrincipal());

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(authentication.getCredentials())) {
            throw new AuthenticationException();
        }

        afterAuthentication(loginMember, response);

        return false;
    }

}
