package nextstep.auth.authentication;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationConverter {

    AuthenticationToken generateAuthentication(HttpServletRequest request) throws Exception;

}
