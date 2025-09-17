package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.repositories;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRepository;
import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserRequestDTO;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.exception.NotFoundException;
import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final JpaUserRepository jpaUserRepository;

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
        return this.jpaUserRepository.findById(id)
            .map(u -> {
                var domain = u.toUserDomain().updatePassword(password);
                return this.save(domain);
            })
            .orElseThrow(() -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    @Override
    public List<JpaUserEntity> findByName(String name) {
        var user = this.jpaUserRepository.findByName(name);
        if (user.isEmpty()) {
            throw new NotFoundException(HttpStatus.NOT_FOUND, "User not found by name.");
        }
        
        return user;
    }

    @Override
    public void delete(String id) {
        this.jpaUserRepository.findById(id)
            .ifPresentOrElse(u -> {
                var domain = u.toUserDomain().deactivate();
                this.save(domain);
            }, 
            () -> new NotFoundException(HttpStatus.NOT_FOUND, "User not found."));
    }

    private JpaUserEntity save(User entity) {
        return this.jpaUserRepository.save(JpaUserEntity.of(entity));
    }

}
