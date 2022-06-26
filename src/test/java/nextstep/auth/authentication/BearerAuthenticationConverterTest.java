package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.auth.authentication.AuthenticationFixture.TOKEN_인증_검증;

class BearerAuthenticationConverterTest {

    public static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9" +
            ".eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";
    private static final String BEARER = "Bearer " + TOKEN;
    public static final String AUTHORIZATION = "Authorization";

    private MockHttpServletRequest request;

    @DisplayName("요청에서 Bearer 정보를 읽고 인증 정보를 생성할 수 있다.")
    @Test
    void convert() {
        // given
        request = token_인증_요청_mock();
        BearerAuthenticationConverter converter = new BearerAuthenticationConverter();

        // when
        AuthenticationToken authentication = converter.convert(request);

        // then
        TOKEN_인증_검증(authentication, TOKEN);
    }

    public static MockHttpServletRequest token_인증_요청_mock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, BEARER);

        return request;
    }

}
