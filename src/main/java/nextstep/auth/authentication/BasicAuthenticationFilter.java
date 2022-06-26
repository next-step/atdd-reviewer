package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.context.Authentication;
import nextstep.auth.context.SecurityContextHolder;
import org.apache.tomcat.util.codec.binary.Base64;

public class BasicAuthenticationFilter extends NonChainingAuthenticationInterceptor {
    private LoadMemberService loginMemberService;

    public BasicAuthenticationFilter(LoadMemberService loginMemberService) {
        this.loginMemberService = loginMemberService;
    }

    @Override
    protected void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) {
        String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
        String authHeader = new String(Base64.decodeBase64(authCredentials));

        String[] splits = authHeader.split(":");
        String principal = splits[0];
        String credentials = splits[1];

        AuthenticationToken token = new AuthenticationToken(principal, credentials);

        LoginMember loginMember = loginMemberService.loadMemberByEmail(token.getPrincipal());
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
