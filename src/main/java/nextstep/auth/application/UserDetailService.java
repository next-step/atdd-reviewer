package nextstep.auth.application;

public interface UserDetailService {
    UserDetail loadUserByUserName(String email);
}
