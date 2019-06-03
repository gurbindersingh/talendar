package at.ac.tuwien.sepm.groupphase.backend.security;

public class AuthenticationConstants {

    public static final String ROLE_PREFIX = "ROLE_";
    public static final String JWT_CLAIM_AUTHORITY = "aut";
    public static final String JWT_CLAIM_PRINCIPAL = "pri";
    public static final String JWT_CLAIM_PRINCIPAL_ID = "pid";
    public static final String TOKEN_PREFIX = "Bearer ";

    private AuthenticationConstants() {
    }
}
