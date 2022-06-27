package nextstep.auth.ui.convert;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Component
public class TokenAuthenticationConverter implements AuthenticationConverter{

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public TokenRequest convert(HttpServletRequest request) throws IOException {
        return objectMapper.readValue(request.getReader(), TokenRequest.class);
    }
}
