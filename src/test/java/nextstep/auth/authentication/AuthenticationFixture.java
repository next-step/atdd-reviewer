package nextstep.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationFixture {

    public static final String EMAIL = "email@email.com";
    public static final String PASSWORD = "password";

    public static void 인증_검증(AuthenticationToken authentication) {
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getCredentials()).isEqualTo(PASSWORD);
    }

}
