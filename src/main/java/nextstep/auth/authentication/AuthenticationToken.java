package nextstep.auth.authentication;

public class AuthenticationToken {
    private String principal;
    private String credentials;

    private AuthenticationToken(String principal, String credentials) {
        this.principal = principal;
        this.credentials = credentials;
    }

    public static AuthenticationToken of(String principal, String credentials) {
        return new AuthenticationToken(principal, credentials);
    }

    public String getPrincipal() {
        return principal;
    }

    public String getCredentials() {
        return credentials;
    }
}
