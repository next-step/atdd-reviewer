package nextstep.subway.unit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.token.JwtTokenProvider;
import nextstep.auth.token.TokenAuthenticationInterceptor2;
import nextstep.auth.token.TokenRequest;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class TokenAuthenticationInterceptorTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    public static final String JWT_TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIiLCJuYW1lIjoiSm9obiBEb2UiLCJpYXQiOjE1MTYyMzkwMjJ9.ih1aovtQShabQ7l0cINw4k1fagApg3qLWiB8Kt59Lno";

    private UserDetailService loginMemberService;
    private JwtTokenProvider jwtTokenProvider;
    private TokenAuthenticationInterceptor2 interceptor;

    @BeforeEach
    void setUp() {
        loginMemberService = mock(UserDetailService.class);
        jwtTokenProvider = mock(JwtTokenProvider.class);
        interceptor = new TokenAuthenticationInterceptor2(loginMemberService, jwtTokenProvider);
    }

    @DisplayName("이메일이 일치하는 회원이 없는 경우 인증 예외 발생")
    @Test
    void non_exists_member() throws Exception {
        //given
        when(loginMemberService.loadUserByUsername(EMAIL)).thenReturn(null);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = interceptor.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("이메일은 일치하지만 패스워드가 일치하지 않는 경우 인증 예외 발생")
    @Test
    void not_matched_password() throws Exception {
        //given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, "wrong_password", 10));
        when(loginMemberService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = interceptor.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("이메일과 패스워드가 모두 일치")
    @Test
    void preHandle() throws Exception {
        //given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 10));
        when(loginMemberService.loadUserByUsername(EMAIL)).thenReturn(loginMember);
        when(jwtTokenProvider.createToken(anyString(), anyList())).thenReturn(JWT_TOKEN);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = interceptor.preHandle(createMockRequest(), response, new Object());

        assertAll(
            () -> assertThat(actual).isFalse(),
            () -> assertThat(response.getStatus()).isEqualTo(HttpServletResponse.SC_OK),
            () -> assertThat(response.getContentType()).isEqualTo(MediaType.APPLICATION_JSON_VALUE)
        );

    }

    private MockHttpServletRequest createMockRequest() throws IOException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        TokenRequest tokenRequest = new TokenRequest(EMAIL, PASSWORD);
        request.setContent(new ObjectMapper().writeValueAsString(tokenRequest).getBytes());
        return request;
    }

}
