package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound;

import java.util.List;

import br.com.fiap.challenge.tech_challenge_fase_01.application.model.user.UserModel;

public interface UserServicePort {

    public UserModel create(UserModel userCreateRequestDTO);

    public UserModel update(String id, UserModel userUpdateRequestDTO);

    public UserModel updatePassword(String id, UserModel updatePasswordRequestDTO);

    public List<UserModel> getByName(String name);

    public void delete(String id);

    public UserModel createOwner(UserModel userCreateRequestDTO);

    public UserModel findAuthenticatedUserByUsername(String username);

}
