package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UpdatePasswordRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.UserWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserCreatePort userCreatePort;
    private final UserCreateOwnerPort userCreateOwnerPort;
    private final UserUpdatePort userUpdatePort;
    private final UserUpdatePasswordPort userUpdatePasswordPort;
    private final UserDeletePort userDeletePort;
    private final UserFindByNamePort userFindByNamePort;
    private final UserFindByIdPort userFindByIdPort;
    private final UserWebMapper webMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createClient(@Valid @RequestBody UserCreateRequestDTO dto) {
        UserCreateRequest request = webMapper.toApplicationRequest(dto);
        UserResponse response = userCreatePort.create(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toWebResponse(response));
    }

    @PostMapping("/owner")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<UserResponseDTO> createOwner(@Valid @RequestBody UserCreateRequestDTO dto) {
        UserCreateRequest request = webMapper.toApplicationRequest(dto);
        UserResponse response = userCreateOwnerPort.createOwner(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(webMapper.toWebResponse(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        UserResponse response = userFindByIdPort.findById(userId);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @GetMapping("/by-name")
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        List<UserResponse> responses = userFindByNamePort.findByName(name);
        return ResponseEntity.ok(responses.stream().map(webMapper::toWebResponse).toList());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or (isAuthenticated() and @userController.isOwnerOfResource(authentication.principal, #id))")
    public ResponseEntity<UserResponseDTO> update(@PathVariable String id,
            @Valid @RequestBody UserUpdateRequestDTO dto) {
        UUID userId = UUID.fromString(id);
        UserUpdateRequest request = webMapper.toApplicationUpdateRequest(dto);
        UserResponse response = userUpdatePort.update(userId, request);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @PatchMapping("/password")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserResponseDTO> updatePassword(@AuthenticationPrincipal SecurityUser principal,
            @Valid @RequestBody UpdatePasswordRequestDTO dto) {
        UUID userId = principal.getId();
        UpdatePasswordRequest request = webMapper.toApplicationPasswordRequest(dto);
        UserResponse response = userUpdatePasswordPort.updatePassword(userId, request);
        return ResponseEntity.ok(webMapper.toWebResponse(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        UUID userId = UUID.fromString(id);
        userDeletePort.delete(userId);
        return ResponseEntity.noContent().build();
    }

    public boolean isOwnerOfResource(SecurityUser principal, String resourceId) {
        if (principal == null || resourceId == null) {
            return false;
        }
        
        try {
            UUID resourceUuid = UUID.fromString(resourceId);
            return principal.getId().equals(resourceUuid);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}