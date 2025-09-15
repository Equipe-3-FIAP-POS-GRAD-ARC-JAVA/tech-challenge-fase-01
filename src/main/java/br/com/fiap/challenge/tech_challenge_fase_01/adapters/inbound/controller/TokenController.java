package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.TokenResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/token")
@RequiredArgsConstructor
public class TokenController {

    private final AuthenticationManager authManager;
    private final JwtService jwtService;

    @PostMapping
    public ResponseEntity<TokenResponse> issueToken(@RequestBody LoginRequest request) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.username(), request.password())
        );

        var roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        String token = jwtService.generate(
                auth.getName(),
                Map.of("roles", roles)
        );

        long exp = Instant.now().plusSeconds(60L * 60L).getEpochSecond(); // mantenha em sincronia com app.jwt.expiration-minutes
        return ResponseEntity.ok(new TokenResponse(token, exp));
    }
}
