package pt.codeflex.auth.security;

public class SecurityConstants {
	public class HEADER_STRING {

	}
	public static final String SECRET = "random51351Key532525ToGenerateJWTs##";
	public static final long EXPIRATION_TIME =  2592000000L; // 30days
	public static final String TOKEN_PREFIX = "Bearer ";
	public static final String HEADER_STRING = "Authorization";
	public static final String SIGN_UP_URL = "/api/account/login";
	public static final String ACESSABLE= "/api/account/**";
}


// https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/