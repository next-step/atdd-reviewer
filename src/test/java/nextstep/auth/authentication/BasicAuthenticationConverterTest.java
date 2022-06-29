package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.auth.authentication.AuthenticationFixture.인증_검증;

class BasicAuthenticationConverterTest {

    private static final String TOKEN = "YWRtaW5AZW1haWwuY29tOnBhc3N3b3Jk";
    private static final String BASIC = "Basic " + TOKEN;
    public static final String AUTHORIZATION = "Authorization";

    private MockHttpServletRequest request;

    @DisplayName("요청에서 인증 정보를 생성할 수 있다.")
    @Test
    void convert() {
        // given
        request = token_인증_요청_mock();
        BasicAuthenticationConverter converter = new BasicAuthenticationConverter();

        // when
        AuthenticationToken authentication = converter.convert(request);

        // then
        인증_검증(authentication);
    }

    public static MockHttpServletRequest token_인증_요청_mock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, BASIC);

        return request;
    }

}
