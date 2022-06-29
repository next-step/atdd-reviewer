package nextstep.auth.domain;

import java.util.List;

public interface LoginUser {

    boolean checkPassword(String password);

    String getEmail();

    List<String> getAuthorities();

}
