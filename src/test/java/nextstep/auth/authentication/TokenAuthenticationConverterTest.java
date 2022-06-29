package nextstep.auth.authentication;

import com.fasterxml.jackson.databind.ObjectMapper;
import nextstep.auth.token.TokenRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import java.io.IOException;

import static nextstep.auth.authentication.AuthenticationFixture.EMAIL;
import static nextstep.auth.authentication.AuthenticationFixture.PASSWORD;
import static nextstep.auth.authentication.AuthenticationFixture.인증_검증;

class TokenAuthenticationConverterTest {

    private MockHttpServletRequest request;

    @DisplayName("요청에서 mail, password 읽고 인증 정보를 생성할 수 있다.")
    @Test
    void convert() throws Exception {
        // given
        request = 인증_요청_mock();
        TokenAuthenticationConverter converter = new TokenAuthenticationConverter();

        // when
        AuthenticationToken authentication = converter.convert(request);

        // then
        인증_검증(authentication);
    }

    public static MockHttpServletRequest 인증_요청_mock() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());

        return request;
    }

}
