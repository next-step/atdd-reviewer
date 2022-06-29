package nextstep.auth.authentication;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthenticationFixture {

    public static final String EMAIL = "admin@email.com";
    public static final String PASSWORD = "password";


    public static void 인증_검증(AuthenticationToken authentication) {
        assertThat(authentication.getPrincipal()).isEqualTo(EMAIL);
        assertThat(authentication.getCredentials()).isEqualTo(PASSWORD);
    }

    public static void TOKEN_인증_검증(AuthenticationToken authentication, String token) {
        assertThat(authentication.getPrincipal()).isEqualTo(token);
        assertThat(authentication.getCredentials()).isEqualTo(token);
    }

}
