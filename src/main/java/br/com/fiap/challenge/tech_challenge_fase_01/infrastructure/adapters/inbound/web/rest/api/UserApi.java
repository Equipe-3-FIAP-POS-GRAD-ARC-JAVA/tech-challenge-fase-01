package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.api;

import java.util.List;

import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserResponseDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Usuários")
@SecurityRequirement(name = "bearerAuth") // JWT aplicado em todos os endpoints deste grupo
public interface UserApi {

    // POST /api/v1/users
    @Operation(summary = "Criar usuário")
    @ApiResponse(responseCode = "201", description = "Criado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Conflito (e-mail/username já existente)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UserResponseDTO> createClient(
            @RequestBody(description = "Dados do novo usuário",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequestDTO.class)))
            UserCreateRequestDTO body
    );

    // POST /api/v1/users/owner
    @Operation(summary = "Criar owner (ADMIN)")
    @ApiResponse(responseCode = "201", description = "Criado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (requer ROLE_ADMIN)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "409", description = "Conflito",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UserResponseDTO> createOwner(
            @RequestBody(description = "Dados do novo owner",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserCreateRequestDTO.class)))
            UserCreateRequestDTO body
    );

    // GET /api/v1/users/{id}
    @Operation(summary = "Buscar usuário por ID")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (requer ADMIN/OWNER)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Não encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UserResponseDTO> getUserById(
            @Parameter(name = "id", description = "UUID do usuário", required = true)
            String id
    );

    // GET /api/v1/users/by-name?name=
    @Operation(summary = "Buscar usuários por nome")
    @ApiResponse(responseCode = "200", description = "OK",
            content = @Content(mediaType = "application/json",
                    array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class))))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (requer ADMIN/OWNER)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<List<UserResponseDTO>> getUserByName(
            @Parameter(name = "name", description = "Nome (LIKE)", required = true)
            String name
    );

    // PUT /api/v1/users/{id}
    @Operation(summary = "Atualizar dados de um usuário")
    @ApiResponse(responseCode = "200", description = "Atualizado",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (sem permissão ou não é o dono do recurso)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UserResponseDTO> update(
            @Parameter(name = "id", description = "UUID do usuário", required = true)
            String id,
            @RequestBody(description = "Dados para atualização",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UserUpdateRequestDTO.class)))
            UserUpdateRequestDTO body
    );

    // PATCH /api/v1/users/password
    @Operation(summary = "Atualizar senha do usuário autenticado")
    @ApiResponse(responseCode = "200", description = "Senha atualizada",
            content = @Content(mediaType = "application/json",
                    schema = @Schema(implementation = UserResponseDTO.class)))
    @ApiResponse(responseCode = "400", description = "Requisição inválida (ex.: senha fraca/confirm. incorreta)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<UserResponseDTO> updatePassword(
            @Parameter(hidden = true) SecurityUser principal,
            @RequestBody(description = "Nova senha",
                    required = true,
                    content = @Content(schema = @Schema(implementation = UpdatePasswordRequestDTO.class)))
            UpdatePasswordRequestDTO body
    );

    // DELETE /api/v1/users/{id}
    @Operation(summary = "Excluir usuário (ADMIN)")
    @ApiResponse(responseCode = "204", description = "Excluído (sem corpo)")
    @ApiResponse(responseCode = "401", description = "Não autenticado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "403", description = "Proibido (requer ROLE_ADMIN)",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "404", description = "Usuário não encontrado",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    @ApiResponse(responseCode = "500", description = "Erro interno",
            content = @Content(mediaType = "application/problem+json",
                    schema = @Schema(implementation = ProblemDetail.class)))
    ResponseEntity<Void> deleteUser(
            @Parameter(name = "id", description = "UUID do usuário", required = true)
            String id
    );
}
