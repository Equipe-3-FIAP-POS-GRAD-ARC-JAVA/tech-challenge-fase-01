package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

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
                UUID.randomUUID(), userId, "Rua A", "123", "São Paulo",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressFindByUser.findAddress(userId)).thenReturn(List.of(appResp));
        when(addressWebMapper.toWebResponse(appResp)).thenReturn(appResp);

        ResponseEntity<List<AddressResponse>> response = controller.getAddress(principal);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(1);
        assertThat(response.getBody().get(0).street()).isEqualTo("Rua A");
        verify(addressFindByUser).findAddress(userId);
    }

    @Test
    void createAddress_createsAndReturnsCreated() {
        UUID userId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        AddressCreateRequestDTO dto = new AddressCreateRequestDTO("Rua B", "99", "Curitiba");
        AddressCreateRequest appReq = new AddressCreateRequest(userId, "Rua B", "99", "Curitiba");
        AddressResponse appResp = new AddressResponse(
                UUID.randomUUID(), userId, "Rua B", "99", "Curitiba",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressWebMapper.toApplicationRequest(dto, userId)).thenReturn(appReq);
        when(addressCreatePort.createAddress(appReq)).thenReturn(appResp);
        when(addressWebMapper.toWebResponse(appResp)).thenReturn(appResp);

        ResponseEntity<AddressResponse> response = controller.createAddress(principal, dto);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().city()).isEqualTo("Curitiba");
        verify(addressCreatePort).createAddress(appReq);
    }

    @Test
    void update_updatesAndReturnsOk() {
        UUID userId = UUID.randomUUID();
        UUID addressId = UUID.randomUUID();
        SecurityUser principal = mock(SecurityUser.class);
        when(principal.getId()).thenReturn(userId);

        AddressUpdateRequestDTO dto = new AddressUpdateRequestDTO("Nova Rua", "10", "Jaú");
        AddressUpdateRequest appReq = new AddressUpdateRequest("Nova Rua", "10", "Jaú");
        AddressResponse appResp = new AddressResponse(
                addressId, userId, "Nova Rua", "10", "Jaú",
                LocalDateTime.now(), LocalDateTime.now()
        );

        when(addressWebMapper.toApplicationUpdateRequest(dto)).thenReturn(appReq);
        when(addressUpdatePort.updateAddress(userId, addressId, appReq)).thenReturn(appResp);
        when(addressWebMapper.toWebResponse(appResp)).thenReturn(appResp);

        ResponseEntity<AddressResponse> response = controller.update(
                principal, addressId.toString(), dto
        );

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().street()).isEqualTo("Nova Rua");
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
