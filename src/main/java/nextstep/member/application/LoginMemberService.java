package nextstep.member.application;

import nextstep.auth.authentication.ILoginMemberService;
import nextstep.auth.authentication.LoginMember;
import nextstep.member.domain.Member;
import nextstep.member.domain.MemberRepository;
import org.springframework.stereotype.Service;

@Service
public class LoginMemberService implements ILoginMemberService {
    private MemberRepository memberRepository;

    public LoginMemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public LoginMember loadMemberByEmail(String email) {
        Member member = memberRepository.findByEmail(email).orElseThrow(RuntimeException::new);
        return new LoginMember(member.getEmail(), member.getPassword(), member.getRoles());
    }
}
