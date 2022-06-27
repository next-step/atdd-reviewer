package nextstep.subway.unit;

import static nextstep.auth.authentication.AuthorizationExtractor.AUTHORIZATION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nextstep.auth.authentication.BasicAuthenticationFilter2;
import nextstep.auth.authentication.UserDetailService;
import nextstep.auth.authentication.UserDetails;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class BasicAuthenticationFilterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";
    private static final String BASIC_TOKEN = "BasicZW1haWxAZW1haWwuY29tOnBhc3N3b3Jk";

    private UserDetailService loginMemberService;
    private BasicAuthenticationFilter2 filter;

    @BeforeEach
    void setUp() {
        loginMemberService = mock(UserDetailService.class);
        filter = new BasicAuthenticationFilter2(loginMemberService);
    }

    @DisplayName("유효하지 않은 경우 인증 예외 발생")
    @Test
    void not_valid_token() {
        //given

        when(loginMemberService.loadUserByUsername(anyString())).thenReturn(null);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("유효하지 않은 경우 인증 예외 발생")
    @Test
    void not_valid_password() {
        //given
        UserDetails loginMember = LoginMember.of(new Member(EMAIL, "wrongpassword", 10));
        when(loginMemberService.loadUserByUsername(anyString())).thenReturn(loginMember);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("정상 인증")
    @Test
    void preHandle() {
        //given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 10));
        when(loginMemberService.loadUserByUsername(anyString())).thenReturn(loginMember);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        //then
        assertAll(
            () -> assertThat(actual).isTrue(),
            () -> assertThat(principal).isEqualTo(EMAIL)
        );
    }

    private MockHttpServletRequest createMockRequest() {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.addHeader(AUTHORIZATION, BASIC_TOKEN);
        return request;
    }
}
