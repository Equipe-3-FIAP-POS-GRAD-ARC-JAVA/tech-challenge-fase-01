package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.mappers.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UpdatePasswordRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserCreateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.requests.UserUpdateRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.UserResponseDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.inbound.UserServiceMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceMapperImpl implements UserServiceMapper {

    private final UserUseCases userService;
    private final UserMapper userMapper;

    @Override
    public UserResponseDTO create(UserCreateRequestDTO userCreateRequestDTO) {
        return userMapper.toResponse(userService.createClient(userMapper.toDomainCreate(userCreateRequestDTO)));
    }

    @Override
    public UserResponseDTO update(String id, UserUpdateRequestDTO userUpdateRequestDTO) {
        return userMapper.toResponse(userService.update(id, userMapper.toDomainUpdate(userUpdateRequestDTO)));
    }

    @Override
    public UserResponseDTO updatePassword(String id, UpdatePasswordRequestDTO updatePasswordRequestDTO) {
        return userMapper.toResponse(userService.updatePassword(id, updatePasswordRequestDTO.getPassword()));
    }

    @Override
    public List<UserResponseDTO> getByName(String name) {
        return userService.findByName(name).stream()
                .map(userMapper::toResponse)
                .toList();
    }

    @Override
    public void delete(String id) {
        userService.delete(id);
    }

    @Override
    public UserResponseDTO createOwner(UserCreateRequestDTO userCreateRequestDTO) {
        return userMapper.toResponse(userService.createOwner(userMapper.toDomainCreate(userCreateRequestDTO)));
    }


//    @Override
//    public User findAuthenticatedUserByUsername(String username) {
//        return userService.findByName(username)
//    }
}
