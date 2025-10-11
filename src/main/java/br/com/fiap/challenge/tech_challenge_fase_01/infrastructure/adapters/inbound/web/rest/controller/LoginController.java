package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

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

/**
 * Controller REST para autenticação.
 * 
 * Responsabilidades (seguindo SOLID):
 * - Receber requisições de login
 * - Converter DTOs web para DTOs da camada de aplicação
 * - Delegar autenticação para o Use Case
 * - Retornar token JWT
 * 
 * Arquitetura Hexagonal:
 * - Esta classe é um Adapter Inbound (driving adapter)
 * - Não contém lógica de negócio ou geração de token
 * - Depende apenas de abstrações (ports)
 */
@RestController
@RequiredArgsConstructor 
@RequestMapping("/api/v1/auth")
public class LoginController {
    
    private final AuthPort authPort;
    private final AuthWebMapper authWebMapper;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        // 1. Converte DTO Web → DTO Application
        var appRequest = authWebMapper.toApplicationLoginRequest(loginRequest);
        
        // 2. Chama Use Case através do Port
        var appResponse = authPort.login(appRequest);
        
        // 3. Converte DTO Application → DTO Web  
        return ResponseEntity.ok(authWebMapper.toWebLoginResponse(appResponse));
    }
}
