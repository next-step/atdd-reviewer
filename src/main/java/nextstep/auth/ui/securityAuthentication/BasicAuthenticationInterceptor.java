package nextstep.auth.ui.securityAuthentication;

import nextstep.auth.application.UserDetail;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.AuthenticationToken;
import nextstep.auth.authentication.AuthorizationExtractor;
import nextstep.auth.authentication.AuthorizationType;
import nextstep.auth.context.Authentication;
import nextstep.member.application.LoginMemberService;
import org.apache.tomcat.util.codec.binary.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class BasicAuthenticationInterceptor extends SecurityAuthenticationInterceptor {
    private final UserDetailService userDetailService;

    public BasicAuthenticationInterceptor(UserDetailService userDetailService) {
        this.userDetailService = userDetailService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        try {
            String authCredentials = AuthorizationExtractor.extract(request, AuthorizationType.BASIC);
            String authHeader = new String(Base64.decodeBase64(authCredentials));

            String[] splits = authHeader.split(":");
            String principal = splits[0];
            String credentials = splits[1];

            AuthenticationToken token = new AuthenticationToken(principal, credentials);

            UserDetail userDetail = userDetailService.loadUserByUserName(token.getPrincipal());
            if (userDetail == null) {
                throw new AuthenticationException();
            }

            if (!userDetail.checkPassword(token.getCredentials())) {
                throw new AuthenticationException();
            }

            Authentication authentication = new Authentication(userDetail.getEmail(), userDetail.getAuthorities());

            afterCompletion(request, response, authentication);

            return true;
        } catch (Exception e) {
            return true;
        }
    }
}
