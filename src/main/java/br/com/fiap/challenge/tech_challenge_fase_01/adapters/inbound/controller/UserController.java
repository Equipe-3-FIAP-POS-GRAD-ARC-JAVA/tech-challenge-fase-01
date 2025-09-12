package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.fiap.challenge.tech_challenge_fase_01.application.service.UserService;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import lombok.RequiredArgsConstructor;


@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/{name}")
    public ResponseEntity<User> getUserByName(@PathVariable("name") String name) {
        return ResponseEntity.ok(userService.findByName(name));
    }
    

}
