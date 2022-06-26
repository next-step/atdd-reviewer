package nextstep.auth.application;

import nextstep.member.domain.LoginMember;

public interface LoginMemberService {

    LoginMember findByUsername(String email);

}
