package nextstep.auth.ui.authentication;

import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.ui.convert.AuthenticationConverter;
import nextstep.member.application.LoginMemberService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationInterceptor extends AuthenticationInterceptor{
    public UsernamePasswordAuthenticationInterceptor(LoginMemberService loginMemberService, AuthenticationConverter authenticationConverter) {
        super(loginMemberService, authenticationConverter);
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws Exception {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
