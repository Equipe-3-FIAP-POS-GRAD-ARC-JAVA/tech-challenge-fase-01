package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.AddressCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUserPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.AddressCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AddressWebMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressFindByUserPort addressFindByUser;
    private final AddressCreatePort addressCreatePort;
    private final AddressDeletePort addressDeletePort;
    private final AddressWebMapper addressWebMapper;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<AddressResponse>> getAddress(@AuthenticationPrincipal SecurityUser principal) {
        List<AddressResponse> responses = addressFindByUser.findAddress(principal.getId());
        return ResponseEntity.ok(responses.stream().map(addressWebMapper::toWebResponse).toList());
    }

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<AddressResponse> createAddress(@AuthenticationPrincipal SecurityUser principal,
                                                         @Valid @RequestBody AddressCreateRequestDTO addressCreateRequestDTO) {
        AddressCreateRequest request = addressWebMapper.toApplicationRequest(addressCreateRequestDTO, principal.getId());
        AddressResponse response = addressCreatePort.createAddress(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(addressWebMapper.toWebResponse(response));
    }

    @DeleteMapping("/{addressId}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteAddress(@AuthenticationPrincipal SecurityUser principal,
                                                     @PathVariable String addressId) {
        UUID addressIdUuid = UUID.fromString(addressId);
        addressDeletePort.deleteAddress(principal.getId(), addressIdUuid);
        return ResponseEntity.noContent().build();
    }

}
