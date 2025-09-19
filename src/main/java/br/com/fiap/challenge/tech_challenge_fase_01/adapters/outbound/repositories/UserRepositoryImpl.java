package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.repositories;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.mappers.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.repository.UserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.shared.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;
    private final UserMapper userMapper;

    @Override
    public JpaUserEntity create(UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public JpaUserEntity update(String id, UserRequestDTO request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public JpaUserEntity updatePassword(String id, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePassword'");
    }

    @Override
    public List<JpaUserEntity> findByName(String name) {
        return this.jpaUserRepository.findByName(name);
    }

    @Override
    public void delete(String id) {
        this.jpaUserRepository.findById(id)
            .ifPresentOrElse(u -> {
                var domain = userMapper.toDomain(u).deactivate();
                this.jpaUserRepository.save(userMapper.toEntity(domain));
            }, 
            () -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public User findByEmail(String email) {
        return this.jpaUserRepository.findByEmail(email)
                .map(userMapper::toDomain) // MapStruct faz a conversão
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found by email: " + email));
    }

    @Override
    public User findByLogin(String login) {
        return this.jpaUserRepository.findByLogin(login)
                .map(userMapper::toDomain) // MapStruct faz a conversão
                .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found by login: " + login));
    }

}
