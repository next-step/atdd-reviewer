package nextstep.auth.application;

import java.util.List;

public interface UserDetail {

    String getEmail();

    String getPassword();

    boolean checkPassword(String password);

    List<String> getAuthorities();
}
