package nextstep.auth.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;

import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NonChainAuthenticationInterceptor {

    public UsernamePasswordAuthenticationFilter(LoginMemberService loginMemberService,
                                                AuthenticationConverter authenticationConverter) {
        super(loginMemberService, authenticationConverter);
    }

    @Override
    public void afterAuthentication(LoginMember loginMember, HttpServletResponse response) {
        Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
