package nextstep.auth.authentication;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;

import static nextstep.auth.authentication.AuthenticationFixture.EMAIL;
import static nextstep.auth.authentication.AuthenticationFixture.PASSWORD;
import static nextstep.auth.authentication.AuthenticationFixture.인증_검증;

class FormAuthenticationConverterTest {

    private MockHttpServletRequest request;

    @DisplayName("요청에서 mail, password 읽고 인증 정보를 생성할 수 있다.")
    @Test
    void convert() {
        // given
        request = 인증_요청_mock();
        FormAuthenticationConverter converter = new FormAuthenticationConverter();

        // when
        AuthenticationToken authentication = converter.convert(request);

        // then
        인증_검증(authentication);
    }

    private MockHttpServletRequest 인증_요청_mock() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addParameter("username", EMAIL);
        request.addParameter("password", PASSWORD);

        return request;
    }

}
