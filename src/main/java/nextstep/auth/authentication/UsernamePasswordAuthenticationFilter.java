package nextstep.auth.authentication;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;

public class UsernamePasswordAuthenticationFilter implements HandlerInterceptor {
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    private ILoginMemberService loginMemberService;

    public UsernamePasswordAuthenticationFilter(ILoginMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            Map<String, String[]> paramMap = request.getParameterMap();
            String username = paramMap.get(USERNAME_FIELD)[0];
            String password = paramMap.get(PASSWORD_FIELD)[0];

            AuthenticationToken token = new AuthenticationToken(username, password);

            String principal = token.getPrincipal();
            LoginMember loginMember = loginMemberService.loadMemberByEmail(principal);

            if (loginMember == null) {
                throw new AuthenticationException();
            }

            if (!loginMember.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(loginMember.getEmail(), loginMember.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);

            return false;
        } catch (Exception e) {
            return true;
        }
    }
}
