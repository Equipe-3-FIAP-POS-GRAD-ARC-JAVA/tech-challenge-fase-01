package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.api.LoginApi;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AuthWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "Autenticação")
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/v1/auth")
public class LoginController implements LoginApi {
    
    private final AuthPort authPort;
    private final AuthWebMapper authWebMapper;


    @PostMapping("/login")
    @Override
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        var appRequest = authWebMapper.toApplicationLoginRequest(loginRequest);
        
        var appResponse = authPort.login(appRequest);
        
        return ResponseEntity.ok(authWebMapper.toWebLoginResponse(appResponse));
    }
}