package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.api;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;


@Tag(name = "Autenticação")
public interface LoginApi {

    @Operation(summary = "Autenticar e obter token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "OK",
                    content = @Content(schema = @Schema(implementation = LoginResponse.class))),
            @ApiResponse(responseCode = "400", description = "Requisição inválida",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class))),
            @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
                    content = @Content(mediaType = "application/problem+json",
                            schema = @Schema(implementation = ProblemDetail.class)))
    })
    ResponseEntity<LoginResponse> login(
            @RequestBody(required = true, description = "Credenciais de login",
                    content = @Content(schema = @Schema(implementation = LoginRequest.class)))
            LoginRequest body
    );
}