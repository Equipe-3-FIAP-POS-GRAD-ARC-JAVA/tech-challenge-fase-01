package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUserPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AddressWebMapper;

class AddressControllerTest {

    @Mock private AddressFindByUserPort addressFindByUser;
    @Mock private AddressCreatePort addressCreatePort;
    @Mock private AddressDeletePort addressDeletePort;
    @Mock private AddressUpdatePort addressUpdatePort;
    @Mock private AddressWebMapper addressWebMapper;

    private AddressController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new AddressController(
                addressFindByUser,
                addressCreatePort,
                addressDeletePort,
                addressWebMapper,
                addressUpdatePort
        );
    }

    @Test
    void getAddress_returnsUserAddresses() {
        UUID userId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        AddressResponse appResp = new AddressResponse(
                UUID.randomUUID(), userId, "Rua A", "123", "Apt 1", "Centro", "São Paulo", "01234-567",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressFindByUser.findAddress(userId)).thenReturn(List.of(appResp));
        when(addressWebMapper.toResponseDTO(appResp)).thenReturn(mock(br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO.class));

        ResponseEntity<List<br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO>> response = controller.getAddresses(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        verify(addressFindByUser).findAddress(userId);
    }

    @Test
    void createAddress_createsAndReturnsCreated() {
        UUID userId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        AddressCreateRequestDTO dto = new AddressCreateRequestDTO("Rua B", "99", "Apt 2", "Vila Nova", "Curitiba", "80030-456");
        AddressCreateRequest appReq = new AddressCreateRequest(userId, "Rua B", "99", "Apt 2", "Vila Nova", "Curitiba", "80030-456");
        AddressResponse appResp = new AddressResponse(
                UUID.randomUUID(), userId, "Rua B", "99", "Apt 2", "Vila Nova", "Curitiba", "80030-456",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressWebMapper.toApplicationRequest(dto, userId)).thenReturn(appReq);
        when(addressCreatePort.createAddress(appReq)).thenReturn(appResp);
        when(addressWebMapper.toResponseDTO(appResp)).thenReturn(mock(br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO.class));

        ResponseEntity<br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO> response = controller.createAddress(principal, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        verify(addressCreatePort).createAddress(appReq);
    }

    @Test
    void update_updatesAndReturnsOk() {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        AddressUpdateRequestDTO dto = new AddressUpdateRequestDTO("Nova Rua", "10", "Casa", "Centro", "Jaú", "17201-000");
        AddressUpdateRequest appReq = new AddressUpdateRequest("Nova Rua", "10", "Casa", "Centro", "Jaú", "17201-000");
        AddressResponse appResp = new AddressResponse(
                addressId, userId, "Nova Rua", "10", "Casa", "Centro", "Jaú", "17201-000",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressWebMapper.toApplicationUpdateRequest(dto)).thenReturn(appReq);
        when(addressUpdatePort.updateAddress(userId, addressId, appReq)).thenReturn(appResp);
        when(addressWebMapper.toResponseDTO(appResp)).thenReturn(mock(br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO.class));

        ResponseEntity<br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO> response = controller.updateAddress(
                principal, addressId.toString(), dto
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(addressUpdatePort).updateAddress(userId, addressId, appReq);
    }

    @Test
    void deleteAddress_removesAndReturnsNoContent() {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        ResponseEntity<Void> response = controller.deleteAddress(principal, addressId.toString());

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        verify(addressDeletePort).deleteAddress(userId, addressId);
    }
}
