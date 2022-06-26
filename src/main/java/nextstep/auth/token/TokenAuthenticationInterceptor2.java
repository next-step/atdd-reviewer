package nextstep.auth.token;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.authentication.AuthenticationConverter;
import nextstep.auth.authentication.NonChainAuthenticationInterceptor;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import org.springframework.http.MediaType;

import javax.servlet.http.HttpServletResponse;

public class TokenAuthenticationInterceptor2 extends NonChainAuthenticationInterceptor {

    private final JwtTokenProvider jwtTokenProvider;

    public TokenAuthenticationInterceptor2(LoginMemberService loginMemberService, JwtTokenProvider jwtTokenProvider,
                                           AuthenticationConverter authenticationGenerator) {
        super(loginMemberService, authenticationGenerator);
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    public void afterAuthentication(LoginMember loginMember, HttpServletResponse response) throws Exception {
        String token = jwtTokenProvider.createToken(loginMember.getEmail(), loginMember.getAuthorities());
        TokenResponse tokenResponse = new TokenResponse(token);

        String responseToClient = new ObjectMapper().writeValueAsString(tokenResponse);
        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getOutputStream().print(responseToClient);
    }

}