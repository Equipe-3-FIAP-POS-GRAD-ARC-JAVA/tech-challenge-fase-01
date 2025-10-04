package br.com.fiap.challenge.tech_challenge_fase_01.application.usecases;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.UserServicePort;

public class UserUseCases implements UserServicePort {
   
    @Override
    public UserModel create(UserModel userCreateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserModel update(String id, UserModel userUpdateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserModel updatePassword(String id, UserModel updatePasswordRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<UserModel> getByName(String name) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void delete(String id) {
        // TODO Auto-generated method stub

    }

    @Override
    public UserModel createOwner(UserModel userCreateRequestDTO) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public UserModel findAuthenticatedUserByUsername(String username) {
        // TODO Auto-generated method stub
        return null;
    }
}
