package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.controller;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.requests.LoginRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.LoginResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.dto.response.UserProfileResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.web.rest.mapper.AuthWebMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class LoginControllerTest {

    @Mock private AuthPort authPort;
    @Mock private AuthWebMapper authWebMapper;

    private LoginController controller;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        controller = new LoginController(authPort, authWebMapper);
    }

    @Test
    void login_returnsOkWithTokenAndProfile() {
        LoginRequest webReq = new LoginRequest("user", "pass");

        UserDomain user = UserDomain.createClient("Name", "email@test.com", "user", "secret123");

        br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse appResp =
                new br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.LoginResponse("tok", user);

        UserProfileResponse profile = new UserProfileResponse(
                user.getId(),
                user.getEmail(),
                List.of("ROLE_CLIENT")
        );
        LoginResponse webResp = new LoginResponse("tok", "Bearer", 3600L, profile);

        when(authWebMapper.toApplicationLoginRequest(webReq))
                .thenReturn(new br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.LoginRequest("user", "pass"));
        when(authPort.login(any())).thenReturn(appResp);
        when(authWebMapper.toWebLoginResponse(appResp)).thenReturn(webResp);

        ResponseEntity<LoginResponse> res = controller.login(webReq);

        assertThat(res.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(res.getBody()).isNotNull();
        assertThat(res.getBody().accessToken()).isEqualTo("tok");
        assertThat(res.getBody().userProfile().email()).isEqualTo("email@test.com");

        verify(authWebMapper).toApplicationLoginRequest(webReq);
        verify(authPort).login(any());
        verify(authWebMapper).toWebLoginResponse(appResp);
    }
}
