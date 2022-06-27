package nextstep.auth.authentication;

import java.util.List;

public interface UserDetails {

    String getEmail();

    boolean checkPassword(String password);

    List<String> getAuthorities();
}
