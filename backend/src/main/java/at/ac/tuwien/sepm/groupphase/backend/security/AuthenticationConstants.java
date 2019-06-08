package at.ac.tuwien.sepm.groupphase.backend.security;

/**
 * Constants in this class are simply used to encode data within the JWT token (kex - values pairs)
 *
 * I.e. these values are the keys, and the effective data are associated with each of these given
 * strings
 */
public class AuthenticationConstants {

    /** this is an exception: not a key but as the name says just a prefix which is used to
     *  retrieve the actual role
     *  NOTE:   when we set role 'ADMIN', spring will encode it as 'ROLE_ADMIN'.
     *          ROLE_ added by default.
     */
    public static final String ROLE_PREFIX = "ROLE_";
    public static final String JWT_CLAIM_AUTHORITY = "aut";
    public static final String JWT_CLAIM_PRINCIPAL = "pri";
    public static final String JWT_CLAIM_PRINCIPAL_ID = "pid";
    public static final String TOKEN_PREFIX = "Bearer ";

    private AuthenticationConstants() {
    }
}
