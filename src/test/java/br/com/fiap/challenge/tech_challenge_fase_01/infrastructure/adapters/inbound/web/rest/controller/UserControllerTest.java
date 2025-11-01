package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class UserControllerTest {

    @Mock private UserCreatePort userCreatePort;
    @Mock private UserCreateOwnerPort userCreateOwnerPort;
    @Mock private UserUpdatePort userUpdatePort;
    @Mock private UserUpdatePasswordPort userUpdatePasswordPort;
    @Mock private UserDeletePort userDeletePort;
    @Mock private UserFindByNamePort userFindByNamePort;
    @Mock private UserFindByIdPort userFindByIdPort;
    @Mock private UserWebMapper webMapper;

    private UserController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new UserController(
                userCreatePort,
                userCreateOwnerPort,
                userUpdatePort,
                userUpdatePasswordPort,
                userDeletePort,
                userFindByNamePort,
                userFindByIdPort,
                webMapper
        );
    }

    @Test
    void createClient_returnsCreated() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO(
                "Name", "email@test.com", "login", "pass", "Rua A", "123", "São Paulo"
        );
        UserCreateRequest appReq = new UserCreateRequest(
                "Name", "email@test.com", "login", "pass", "Rua A", "123", "São Paulo"
        );
        UserResponse appResp = new UserResponse(UUID.randomUUID(), "Name", "email@test.com", "login");
        UserResponseDTO webResp = new UserResponseDTO(appResp.id().toString(), appResp.name(), appResp.email(), appResp.login());

        when(webMapper.toApplicationRequest(dto)).thenReturn(appReq);
        when(userCreatePort.create(appReq)).thenReturn(appResp);
        when(webMapper.toWebResponse(appResp)).thenReturn(webResp);

        ResponseEntity<UserResponseDTO> response = controller.createClient(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().name()).isEqualTo("Name");
        verify(userCreatePort).create(appReq);
    }

    @Test
    void createOwner_returnsCreated() {
        UserCreateRequestDTO dto = new UserCreateRequestDTO(
                "Owner", "owner@test.com", "owner", "pass", "Rua B", "99", "Curitiba"
        );
        UserCreateRequest appReq = new UserCreateRequest(
                "Owner", "owner@test.com", "owner", "pass", "Rua B", "99", "Curitiba"
        );
        UserResponse appResp = new UserResponse(UUID.randomUUID(), "Owner", "owner@test.com", "owner");
        UserResponseDTO webResp = new UserResponseDTO(appResp.id().toString(), appResp.name(), appResp.email(), appResp.login());

        when(webMapper.toApplicationRequest(dto)).thenReturn(appReq);
        when(userCreateOwnerPort.createOwner(appReq)).thenReturn(appResp);
        when(webMapper.toWebResponse(appResp)).thenReturn(webResp);

        ResponseEntity<UserResponseDTO> response = controller.createOwner(dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().login()).isEqualTo("owner");
        verify(userCreateOwnerPort).createOwner(appReq);
    }

    @Test
    void getUserById_returnsOk() {
        UUID id = UUID.randomUUID();
        UserResponse appResp = new UserResponse(id, "Name", "email@test.com", "login");
        UserResponseDTO webResp = new UserResponseDTO(id.toString(), "Name", "email@test.com", "login");

        when(userFindByIdPort.findById(id)).thenReturn(appResp);
        when(webMapper.toWebResponse(appResp)).thenReturn(webResp);

        ResponseEntity<UserResponseDTO> res = controller.getUserById(id.toString());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().id()).isEqualTo(id.toString());
        verify(userFindByIdPort).findById(id);
    }

    @Test
    void getUserByName_returnsList() {
        UserResponse u = new UserResponse(UUID.randomUUID(), "Na", "e@t", "l");
        UserResponseDTO w = new UserResponseDTO(u.id().toString(), u.name(), u.email(), u.login());

        when(userFindByNamePort.findByName("Na")).thenReturn(List.of(u));
        when(webMapper.toWebResponse(u)).thenReturn(w);

        ResponseEntity<List<UserResponseDTO>> res = controller.getUserByName("Na");

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody()).hasSize(1);
        assertThat(res.getBody().get(0).name()).isEqualTo("Na");
        verify(userFindByNamePort).findByName("Na");
    }

    @Test
    void update_returnsOk() {
        UUID id = UUID.randomUUID();
        UserUpdateRequestDTO dto = new UserUpdateRequestDTO("New", "new@e", "newLogin");
        UserUpdateRequest appReq = new UserUpdateRequest("New", "new@e", "newLogin");
        UserResponse appResp = new UserResponse(id, "New", "new@e", "newLogin");
        UserResponseDTO webResp = new UserResponseDTO(id.toString(), "New", "new@e", "newLogin");

        when(webMapper.toApplicationUpdateRequest(dto)).thenReturn(appReq);
        when(userUpdatePort.update(id, appReq)).thenReturn(appResp);
        when(webMapper.toWebResponse(appResp)).thenReturn(webResp);

        ResponseEntity<UserResponseDTO> res = controller.update(id.toString(), dto);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().login()).isEqualTo("newLogin");
        verify(userUpdatePort).update(id, appReq);
    }

    @Test
    void updatePassword_returnsOk() {
        UUID id = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(id);

        UpdatePasswordRequestDTO dto = new UpdatePasswordRequestDTO("old", "new", "new");
        UpdatePasswordRequest appReq = new UpdatePasswordRequest("old", "new", "new");
        UserResponse appResp = new UserResponse(id, "Name", "email@test.com", "login");
        UserResponseDTO webResp = new UserResponseDTO(id.toString(), "Name", "email@test.com", "login");

        when(webMapper.toApplicationPasswordRequest(dto)).thenReturn(appReq);
        when(userUpdatePasswordPort.updatePassword(id, appReq)).thenReturn(appResp);
        when(webMapper.toWebResponse(appResp)).thenReturn(webResp);

        ResponseEntity<UserResponseDTO> res = controller.updatePassword(principal, dto);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().id()).isEqualTo(id.toString());
        verify(userUpdatePasswordPort).updatePassword(id, appReq);
        verify(webMapper).toApplicationPasswordRequest(dto);
    }

    @Test
    void delete_returnsNoContent() {
        UUID id = UUID.randomUUID();

        ResponseEntity<Void> res = controller.deleteUser(id.toString());

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(userDeletePort).delete(id);
    }

    @Test
    void isOwnerOfResource_validatesUuidAndId() {
        SecurityUser principal = mock(SecurityUser.class);
        UUID userId = UUID.randomUUID();
        when(principal.getId()).thenReturn(userId);

        assertThat(controller.isOwnerOfResource(principal, userId.toString())).isTrue();
        assertThat(controller.isOwnerOfResource(principal, "not-a-uuid")).isFalse();
        assertThat(controller.isOwnerOfResource(null, userId.toString())).isFalse();
    }
}
