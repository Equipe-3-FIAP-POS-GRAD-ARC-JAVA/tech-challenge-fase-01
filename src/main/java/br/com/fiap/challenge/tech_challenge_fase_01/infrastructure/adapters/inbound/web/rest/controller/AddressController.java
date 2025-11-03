package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressUpdateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUserPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.api.AddressApi;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AddressWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController implements AddressApi {

    private final AddressFindByUserPort addressFindByUser;
    private final AddressCreatePort addressCreatePort;
    private final AddressDeletePort addressDeletePort;
    private final AddressWebMapper addressWebMapper;
    private final AddressUpdatePort addressUpdatePort;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<List<AddressResponseDTO>> getAddresses(@AuthenticationPrincipal SecurityUser principal) {
        List<AddressResponse> responses = addressFindByUser.findAddress(principal.getId());
        List<AddressResponseDTO> responseDTOs = responses.stream()
                .map(addressWebMapper::toResponseDTO)
                .toList();
        return ResponseEntity.ok(responseDTOs);
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<AddressResponseDTO> createAddress(@AuthenticationPrincipal SecurityUser principal,
                                                         @Valid @RequestBody AddressCreateRequestDTO addressCreateRequestDTO) {
        AddressCreateRequest request = addressWebMapper.toApplicationRequest(addressCreateRequestDTO, principal.getId());
        AddressResponse response = addressCreatePort.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressWebMapper.toResponseDTO(response));
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal SecurityUser principal,
                                              @PathVariable String addressId) {
        UUID addressIdUuid = UUID.fromString(addressId);
        addressDeletePort.deleteAddress(principal.getId(), addressIdUuid);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    @Override
    public ResponseEntity<AddressResponseDTO> updateAddress(
            @AuthenticationPrincipal SecurityUser principal, @PathVariable String addressId,
            @Valid @RequestBody AddressUpdateRequestDTO dto) {
        UUID addressIdUuid = UUID.fromString(addressId);
        UUID userId = principal.getId();
        AddressUpdateRequest request = addressWebMapper.toApplicationUpdateRequest(dto);
        AddressResponse response = addressUpdatePort.updateAddress(userId, addressIdUuid, request);
        return ResponseEntity.ok(addressWebMapper.toResponseDTO(response));
    }

}
