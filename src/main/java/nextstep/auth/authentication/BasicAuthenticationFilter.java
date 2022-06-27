package nextstep.auth.authentication;

import nextstep.auth.application.AuthenticationUserService;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.domain.LoginUser;

public class BasicAuthenticationFilter extends ChainAuthenticationInterceptor {

    private final AuthenticationUserService authenticationUserService;

    public BasicAuthenticationFilter(AuthenticationUserService loginMemberService,
                                     AuthenticationConverter authenticationConverter) {
        super(authenticationConverter);

        this.authenticationUserService = loginMemberService;
    }

    @Override
    public void afterAuthentication(AuthenticationToken token) {
        LoginUser loginUser = authenticationUserService.loadUserByUsername(token.getPrincipal());
        if (loginUser == null) {
            throw new AuthenticationException();
        }

        if (!loginUser.checkPassword(token.getCredentials())) {
            throw new AuthenticationException();
        }

        Authentication authentication = new Authentication(loginUser.getEmail(), loginUser.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

}
