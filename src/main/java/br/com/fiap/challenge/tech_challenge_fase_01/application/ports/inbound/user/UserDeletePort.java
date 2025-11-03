package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user;

import java.util.UUID;

public interface UserDeletePort {

    void delete(UUID id);

}
