package nextstep.auth.authentication;

import nextstep.auth.application.AuthenticationUserService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.domain.LoginUser;

import javax.servlet.http.HttpServletResponse;

public class UsernamePasswordAuthenticationFilter extends NonChainAuthenticationInterceptor {

    public UsernamePasswordAuthenticationFilter(AuthenticationUserService authenticationUserService,
                                                AuthenticationConverter authenticationConverter) {
        super(authenticationUserService, authenticationConverter);
    }

    @Override
    public void afterComplete(LoginUser loginUser, HttpServletResponse response) {
        Authentication authentication = new Authentication(loginUser.getEmail(), loginUser.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
