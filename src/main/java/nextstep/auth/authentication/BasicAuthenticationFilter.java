package nextstep.auth.authentication;

import nextstep.auth.application.LoginMemberService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;

public class BasicAuthenticationFilter extends ChainAuthenticationInterceptor {

    private final LoginMemberService loginMemberService;

    public BasicAuthenticationFilter(LoginMemberService loginMemberService,
                                     AuthenticationConverter authenticationConverter) {
        super(authenticationConverter);
        this.loginMemberService = loginMemberService;
    }

    public void afterAuthentication(AuthenticationToken token) {

        LoginMember loginMember = loginMemberService.findByUsername(token.getPrincipal());
        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);

    }

}
