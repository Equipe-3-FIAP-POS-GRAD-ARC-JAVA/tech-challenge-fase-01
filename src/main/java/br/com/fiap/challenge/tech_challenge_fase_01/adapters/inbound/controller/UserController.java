package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.application.service.UserServiceImpl;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserServiceImpl userService;

    @GetMapping("/by-name")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteUser(@RequestParam String id) {
        userService.delete(id);
        return ResponseEntity.noContent().build();
    }
    

}
