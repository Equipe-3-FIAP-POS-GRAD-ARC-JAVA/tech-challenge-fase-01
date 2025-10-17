package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.AddressResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.address.AddressFindByUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.SecurityUser;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.AddressResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AddressWebMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/address")
@RequiredArgsConstructor
public class AddressController {

    private final AddressFindByUser addressFindByUser;
    private final AddressWebMapper addressWebMapper;

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'OWNER')")
    public ResponseEntity<List<AddressResponseDTO>> getAddress(@AuthenticationPrincipal SecurityUser principal) {
        List<AddressResponse> responses = addressFindByUser.findAddress(principal.getId());
        return ResponseEntity.ok(responses.stream().map(addressWebMapper::toWebResponse).toList());
    }
}
