package nextstep.subway.unit;

import static nextstep.auth.authentication.UsernamePasswordAuthenticationFilter.PASSWORD_FIELD;
import static nextstep.auth.authentication.UsernamePasswordAuthenticationFilter.USERNAME_FIELD;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import nextstep.auth.authentication.UsernamePasswordAuthenticationFilter;
import nextstep.auth.context.SecurityContext;
import nextstep.auth.context.SecurityContextHolder;
import nextstep.member.application.LoginMemberService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

class UsernamePasswordAuthenticationFilterTest {

    private static final String EMAIL = "email@email.com";
    private static final String PASSWORD = "password";

    private LoginMemberService loginMemberService;
    private UsernamePasswordAuthenticationFilter filter;

    @BeforeEach
    void setUp() {
        loginMemberService = mock(LoginMemberService.class);
        filter = new UsernamePasswordAuthenticationFilter(loginMemberService);
    }

    @DisplayName("이메일이 일치하는 회원이 없는 경우 인증 예외 발생")
    @Test
    void non_exists_member() {
        //given
        when(loginMemberService.loadUserByUsername(anyString())).thenReturn(null);

        //when
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean actual = filter.preHandle(createMockRequestByEmail(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("이메일은 일치하지만 패스워드가 일치하는 회원이 없는 경우 인증 예외 발생")
    @Test
    void not_matched_password() {
        //given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, "wrong_password", 10));
        when(loginMemberService.loadUserByUsername(anyString())).thenReturn(loginMember);

        //when
        MockHttpServletResponse response = new MockHttpServletResponse();

        boolean actual = filter.preHandle(createMockRequest(), response, new Object());

        //then
        assertThat(actual).isTrue();
    }

    @DisplayName("이메일과 패스워드가 모두 일치")
    @Test
    void preHandle() {
        //given
        LoginMember loginMember = LoginMember.of(new Member(EMAIL, PASSWORD, 10));
        when(loginMemberService.loadUserByUsername(EMAIL)).thenReturn(loginMember);

        MockHttpServletResponse response = new MockHttpServletResponse();

        //when
        boolean actual = filter.preHandle(createMockRequest(), response, new Object());

        SecurityContext context = SecurityContextHolder.getContext();
        Object principal = context.getAuthentication().getPrincipal();

        //then
        assertAll(
            () -> assertThat(actual).isFalse(),
            () -> assertThat(principal).isEqualTo(EMAIL)
        );

    }

    private MockHttpServletRequest createMockRequest() {
        return getMockHttpServletRequest(EMAIL);
    }

    private MockHttpServletRequest createMockRequestByEmail() {
        return getMockHttpServletRequest("nonExistsEmail");
    }

    private MockHttpServletRequest getMockHttpServletRequest(String nonExistsEmail) {
        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setParameter(USERNAME_FIELD, nonExistsEmail);
        request.setParameter(PASSWORD_FIELD, PASSWORD);
        return request;
    }
}
