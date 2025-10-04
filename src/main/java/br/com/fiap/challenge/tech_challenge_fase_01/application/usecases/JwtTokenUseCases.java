package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.AuthServicePort;

public class JwtTokenUseCases implements AuthServicePort {

    @Override
    public UserModel findAuthenticatedUserByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserModel login(UserModel request) {
        // TODO Auto-generated method stub
        return null;
    }
    
    
}
