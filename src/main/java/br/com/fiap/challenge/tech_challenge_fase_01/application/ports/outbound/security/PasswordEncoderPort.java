package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security;

/**
 * Port Outbound para criptografia de senhas.
 * 
 * Seguindo a Arquitetura Hexagonal:
 * - Define o contrato para adaptadores de segurança
 * - Não conhece implementação específica (BCrypt, Argon2, etc)
 * - Permite trocar a implementação sem afetar o domínio
 * 
 * A implementação na camada de infraestrutura usará
 * o algoritmo de hash mais adequado (ex: BCryptPasswordEncoder).
 */
public interface PasswordEncoderPort {

    /**
     * Criptografa uma senha em texto plano.
     * 
     * @param rawPassword Senha em texto plano
     * @return Senha criptografada (hash)
     */
    String encode(String rawPassword);

    /**
     * Verifica se uma senha em texto plano corresponde ao hash.
     * 
     * @param rawPassword     Senha em texto plano
     * @param encodedPassword Senha criptografada (hash)
     * @return true se corresponde, false caso contrário
     */
    boolean matches(String rawPassword, String encodedPassword);
}
