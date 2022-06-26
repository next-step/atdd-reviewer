package nextstep.auth.authentication;

public interface ILoginMemberService {

	LoginMember loadMemberByEmail(String email);
}
