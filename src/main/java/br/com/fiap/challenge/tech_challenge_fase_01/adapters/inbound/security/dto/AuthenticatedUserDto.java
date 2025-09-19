package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.dto;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.enumx.RolesEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private String name;

    private String username;

    private String password;

    private List<RolesEnum> userRole;

}
