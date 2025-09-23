package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import java.util.List;

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

import br.com.fiap.challenge.tech_challenge_fase_01.application.service.UserService;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserUpdateRequestDTO;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponseDTO> createClient(@RequestBody UserCreateRequestDTO dto) {
        var user = this.userService.createClient(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PutMapping
    public ResponseEntity<UserResponseDTO> update(@RequestParam String id, @RequestBody UserUpdateRequestDTO dto) {
        var user = this.userService.update(id, dto);
        return ResponseEntity.ok(user);
    }

    @PatchMapping
    public ResponseEntity<UserResponseDTO> updatePassword(@RequestBody UpdatePasswordRequestDTO dto) {
        var user = this.userService.updatePassword(dto.id(), dto.password());
        return ResponseEntity.ok(user);
    }

    @GetMapping("/by-name")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(this.userService.findByName(name));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String id) {
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/owner")
    public ResponseEntity<UserResponseDTO> createOwner(@RequestBody UserCreateRequestDTO dto) {
        var user = this.userService.createOwner(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

}
