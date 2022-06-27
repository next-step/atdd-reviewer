package nextstep.auth.ui.convert;

import nextstep.auth.token.TokenRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

public interface AuthenticationConverter {
    TokenRequest convert(HttpServletRequest request) throws IOException;
}
