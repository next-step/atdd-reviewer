package nextstep.auth.authentication;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;

public class UsernamePasswordAuthenticationFilter extends NonChainAuthenticationFilter {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private UserDetailService loginMemberService;

    public UsernamePasswordAuthenticationFilter(UserDetailService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        AuthenticationToken token = new AuthenticationToken(username, password);

        String principal = token.getPrincipal();
        UserDetails loginMember = loginMemberService.loadUserByUsername(principal);

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
