package nextstep.subway.unit;

import static nextstep.auth.authentication.AuthorizationExtractor.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nextstep.auth.authentication.BearerTokenAuthenticationFilter;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.auth.token.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class BearerTokenAuthenticationFilterTest {

    private static final String EMAIL = "email@email.com";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private JwtTokenProvider jwtTokenProvider;
    private BearerTokenAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        jwtTokenProvider = mock(JwtTokenProvider.class);
        filter = new BearerTokenAuthenticationFilter(jwtTokenProvider);
    }

    @DisplayName("유효하지 않은 경우 인증 예외 발생")
    @Test
    void not_valid_token() {
        //given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(false);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("유효한 토큰")
    @Test
    void preHandle() {
        //given
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getPrincipal(anyString())).thenReturn(EMAIL);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        assertAll(
            () -> assertThat(actual).isTrue(),
            () -> assertThat(principal).isEqualTo(EMAIL)
        );

    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, "Bearer"+JWT_TOKEN);
        return request;
    }
}
