package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserResponseDTO {
    private String id;
    private String name;
    private String email;
    private String login;
}
