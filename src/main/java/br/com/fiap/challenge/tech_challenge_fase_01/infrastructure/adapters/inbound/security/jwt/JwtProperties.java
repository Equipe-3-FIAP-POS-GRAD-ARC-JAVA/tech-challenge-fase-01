package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;

    private String secretKey;

    private long expirationMinute;

}
