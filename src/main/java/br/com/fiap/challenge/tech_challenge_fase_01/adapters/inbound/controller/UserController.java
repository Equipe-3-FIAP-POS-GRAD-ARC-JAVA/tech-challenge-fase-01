package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound.UserServiceMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceMapper userServiceMapper;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createClient(@RequestBody UserCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceMapper.create(dto));
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestParam String id, @RequestBody UserUpdateRequestDTO dto) {
        return ResponseEntity.ok(userServiceMapper.update(id, dto));
    }

    @PatchMapping
    public ResponseEntity<UserResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO dto) {
        return ResponseEntity.ok(userServiceMapper.updatePassword(dto.getId(), dto));
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {

        return ResponseEntity.ok(userServiceMapper.getByName(name));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String id) {
        userServiceMapper.getByName(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/owner")
    public ResponseEntity<UserResponseDTO> createOwner(@RequestBody UserCreateRequestDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(userServiceMapper.createOwner(dto));
    }

}
