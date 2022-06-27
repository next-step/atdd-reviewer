package nextstep.member.application;

import nextstep.auth.application.AuthenticationUserService;
import nextstep.member.domain.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements AuthenticationUserService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember loadUserByUsername(String username) {
        Member member = memberRepository.findByEmail(username).orElseThrow(RuntimeException::new);
        return LoginMember.of(member);
    }
}
