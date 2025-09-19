package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String issuer;

    private String secretKey;

    private long expirationMinute;

}
