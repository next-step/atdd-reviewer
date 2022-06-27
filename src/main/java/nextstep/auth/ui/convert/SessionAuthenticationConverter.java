package nextstep.auth.ui.convert;

import nextstep.auth.token.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

@Component
public class SessionAuthenticationConverter implements AuthenticationConverter{
    public static final String USERNAME_FIELD = "username";
    public static final String PASSWORD_FIELD = "password";

    @Override
    public TokenRequest convert(HttpServletRequest request) throws IOException {
        Map<String, String[]> paramMap = request.getParameterMap();
        String principal = paramMap.get(USERNAME_FIELD)[0];
        String credentials = paramMap.get(PASSWORD_FIELD)[0];

        return new TokenRequest(principal, credentials);
    }
}
