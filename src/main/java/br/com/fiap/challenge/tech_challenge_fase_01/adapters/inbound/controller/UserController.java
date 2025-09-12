package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.challenge.tech_challenge_fase_01.application.service.UserService;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/by-name")
    public ResponseEntity<List<UserResponseDTO>> getUserByName(@RequestParam String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }
    

}
