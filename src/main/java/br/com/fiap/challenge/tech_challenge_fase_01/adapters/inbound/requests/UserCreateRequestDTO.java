package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserCreateRequestDTO {
    private String name;
    private String email;
    private String login;
    private String password;
}
