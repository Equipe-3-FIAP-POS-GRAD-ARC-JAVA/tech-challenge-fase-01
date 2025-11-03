package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.health;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

@Component("userService")
@RequiredArgsConstructor
public class UserServiceHealthIndicator implements HealthIndicator {

    private final UserRepositoryPort userRepositoryPort;

    @Override
    public Health health() {
        try {

            userRepositoryPort.findByName("teste");
            
            return Health.up()
                .withDetail("status", "User repository is accessible")
                .withDetail("component", "UserRepository")
                .withDetail("connectionTest", "Successfully executed query")
                .build();
                
        } catch (Exception e) {
            return Health.down()
                .withDetail("error", e.getMessage())
                .withDetail("component", "UserRepository")
                .withDetail("connectionTest", "Failed to execute query")
                .withException(e)
                .build();
        }
    }
}