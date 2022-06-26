package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public final class FormAuthenticationConverter implements AuthenticationConverter {

    private static final String USERNAME_FIELD = "username";
    private static final String PASSWORD_FIELD = "password";

    @Override
    public AuthenticationToken convert(HttpServletRequest request) {
        Map<String, String[]> paramMap = request.getParameterMap();
        String username = paramMap.get(USERNAME_FIELD)[0];
        String password = paramMap.get(PASSWORD_FIELD)[0];

        return AuthenticationToken.of(username, password);
    }

}
