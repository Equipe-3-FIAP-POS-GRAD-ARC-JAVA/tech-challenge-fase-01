package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security.dto;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.enumx.RolesEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthenticatedUserDto {

    private String name;

    private String username;

    private String password;

    private List<RolesEnum> userRole;

}
