package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.AuthenticationUserService;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.NonChainAuthenticationInterceptor;
import nextstep.auth.domain.LoginUser;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationInterceptor extends NonChainAuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(AuthenticationUserService authenticationUserService,
                                          JwtTokenProvider jwtTokenProvider,
                                          AuthenticationConverter authenticationConverter) {
        super(authenticationUserService, authenticationConverter);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterComplete(LoginUser loginUser, HttpServletResponse response) throws Exception {
        String token = jwtTokenProvider.createToken(loginUser.getEmail(), loginUser.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}
