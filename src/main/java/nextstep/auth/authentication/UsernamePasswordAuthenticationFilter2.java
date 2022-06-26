package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter2 extends NonChainAuthenticationInterceptor {

    public UsernamePasswordAuthenticationFilter2(LoginMemberService loginMemberService,
                                                 AuthenticationConverter authenticationGenerator) {
        super(loginMemberService, authenticationGenerator);
    }

    @Override
    public void afterAuthentication(LoginMember loginMember, HttpServletResponse response) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
