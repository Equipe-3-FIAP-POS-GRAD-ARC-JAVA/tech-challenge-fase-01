package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security;

public interface PasswordEncoderPort {

    String encode(String rawPassword);

    boolean matches(String rawPassword, String encodedPassword);
}