package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs.constants;

public class SecurityConstants {

	/**
	 * Token Prefix
	 * We will use this prefix when parsing JWT Token
	 */
	public static final String TOKEN_PREFIX = "Bearer ";

	/**
	 * Authorization Prefix in HttpServletRequest
	 * Authorization: <type> <credentials>
	 * For Example : Authorization: Bearer YWxhZGxa1qea32GVuc2VzYW1l
	 */
	public static final String HEADER_STRING = "Authorization";

	private SecurityConstants() {

		throw new UnsupportedOperationException();
	}

}
