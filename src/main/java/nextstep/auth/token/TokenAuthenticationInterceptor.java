package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.ChainingAuthenticationInterceptor;
import nextstep.auth.authentication.LoadMemberService;
import nextstep.auth.authentication.LoginMember;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor extends ChainingAuthenticationInterceptor {
    private LoadMemberService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor(LoadMemberService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void authenticate(HttpServletRequest request, HttpServletResponse response, Object handler) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        LoginMember loginMember = loginMemberService.loadMemberByEmail(principal);

        if (loginMember == null) {
            throw new AuthenticationException();
        }

        if (!loginMember.checkPassword(credentials)) {
            throw new AuthenticationException();
        }

        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }
}
