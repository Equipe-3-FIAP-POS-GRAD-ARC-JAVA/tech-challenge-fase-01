package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdatePasswordRequestDTO {
    private String id;
    private String password;
}
