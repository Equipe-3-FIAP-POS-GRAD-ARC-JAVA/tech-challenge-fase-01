package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.api;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Endereços")
@SecurityRequirement(name = "bearerAuth")
public interface AddressApi {

    // GET /api/v1/address
    @Operation(summary = "Listar endereços do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class))))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<List<AddressResponseDTO>> getAddresses(
            @Parameter(hidden = true) SecurityUser principal
    );

    // POST /api/v1/address
    @Operation(summary = "Criar novo endereço do usuário autenticado")
    @ApiResponse(responseCode = "201", description = "Criado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddressResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<AddressResponseDTO> createAddress(
            @Parameter(hidden = true) SecurityUser principal,
            @RequestBody(description = "Dados do novo endereço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddressCreateRequestDTO.class)))
            AddressCreateRequestDTO body
    );

    // PUT /api/v1/address/{addressId}
    @Operation(summary = "Atualizar endereço do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Atualizado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = AddressResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (sem permissão para este endereço)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<AddressResponseDTO> updateAddress(
            @Parameter(hidden = true) SecurityUser principal,
            @Parameter(name = "addressId", description = "UUID do endereço", required = true)
            String addressId,
            @RequestBody(description = "Dados para atualização do endereço",
                    required = true,
                    content = @Content(schema = @Schema(implementation = AddressUpdateRequestDTO.class)))
            AddressUpdateRequestDTO body
    );

    // DELETE /api/v1/address/{addressId}
    @Operation(summary = "Excluir endereço")
    @ApiResponse(responseCode = "204", description = "Excluído (sem corpo)")
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (sem permissão para este endereço)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Endereço não encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> deleteAddress(
            @Parameter(hidden = true) SecurityUser principal,
            @Parameter(name = "addressId", description = "UUID do endereço", required = true)
            String addressId
    );
}