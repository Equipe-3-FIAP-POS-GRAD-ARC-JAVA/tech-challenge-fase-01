package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound.AuthServiceMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class LoginController {

    private final AuthServiceMapper authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> loginRequest(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }
}
