package br.com.fiap.challenge.tech_challenge_fase_01.application.mapper;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import org.junit.jupiter.api.Test;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;

class UserMapperTest {

    @Test
    void toResponse_shouldMapAllFields(){
        UUID id = UUID.randomUUID();
        UserDomain domain = UserDomain.builder()
                .id(id)
                .name(PersonName.of("Jane"))
                .email(Email.of("jane@ex.com"))
                .login(Username.of("jane"))
                .password("secret")
                .role(List.of(RolesEnum.CLIENT))
                .createdAt(LocalDateTime.now().minusDays(2))
                .updatedAt(LocalDateTime.now())
                .build();

        UserResponse res = UserMapper.toResponse(domain);
        assertNotNull(res);
        assertEquals(id, res.id());
        assertEquals("Jane", res.name());
        assertEquals("jane@ex.com", res.email());
        assertEquals("jane", res.login());
    }

    @Test
    void toResponse_whenNull_shouldReturnNull(){
        assertNull(UserMapper.toResponse(null));
    }

    @Test
    void toResponseList_whenNullOrEmpty_shouldReturnEmpty(){
        assertTrue(UserMapper.toResponseList(null).isEmpty());
        assertTrue(UserMapper.toResponseList(List.of()).isEmpty());
    }
}
