package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.AuthenticationException;
import nextstep.auth.authentication.NonChainAuthenticationFilter;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.authentication.UserDetails;
import org.springframework.http.MediaType;

public class TokenAuthenticationInterceptor2 extends NonChainAuthenticationFilter {
    private UserDetailService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor2(UserDetailService loginMemberService, JwtTokenProvider jwtTokenProvider) {
        this.loginMemberService = loginMemberService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void handle(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String content = request.getReader().lines().collect(Collectors.joining(System.lineSeparator()));
        TokenRequest tokenRequest = new ObjectMapper().readValue(content, TokenRequest.class);

        String principal = tokenRequest.getEmail();
        String credentials = tokenRequest.getPassword();

        UserDetails loginMember = loginMemberService.loadUserByUsername(principal);

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
