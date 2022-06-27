package nextstep.auth.ui.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.application.UserDetailService;
import nextstep.auth.context.Authentication;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenResponse;
import nextstep.auth.ui.convert.AuthenticationConverter;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationInterceptor extends AuthenticationInterceptor{

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(UserDetailService userDetailService, AuthenticationConverter authenticationConverter, JwtTokenProvider jwtTokenProvider) {
        super(userDetailService, authenticationConverter);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws Exception {
        String token = jwtTokenProvider.createToken(authentication.getPrincipal().toString(), authentication.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
