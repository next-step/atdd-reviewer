package nextstep.auth.application;

import nextstep.auth.domain.LoginUser;

public interface AuthenticationUserService {

    LoginUser loadUserByUsername(String username);

}
