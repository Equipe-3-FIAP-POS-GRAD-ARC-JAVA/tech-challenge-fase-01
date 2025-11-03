package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserAlreadyExistsException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

public class UserDomainService {

    private final UserRepositoryPort userRepository;

    public UserDomainService(UserRepositoryPort userRepository) {
        this.userRepository = userRepository;
    }

    public void ensureUsernameIsUnique(Username username) {
        if (userRepository.existsByUsername(username.getValue())) {
            throw new UserAlreadyExistsException(
                    "Já existe um usuário com o login: " + username.getValue());
        }
    }

    public void ensureUsernameIsUnique(String username) {
        ensureUsernameIsUnique(Username.of(username));
    }

    public void ensureEmailIsUnique(Email email) {
        userRepository.findByEmail(email.getValue()).ifPresent(user -> {
            throw new UserAlreadyExistsException(
                    "Já existe um usuário com o email: " + email.getValue());
        });
    }

    public void ensureCanBeDeactivated(UserDomain user) {
        if (!user.isActive()) {
            throw new BusinessRuleException("Usuário já está inativo");
        }

    }

    public void ensureCanBeDeleted(UserDomain user) {
        if (user.isActive()) {
            throw new BusinessRuleException(
                    "Apenas usuários inativos podem ser excluídos. Desative o usuário primeiro.");
        }

    }

    public void ensureUserIsUnique(String username, String email) {
        ensureUsernameIsUnique(username);

    }

    public void ensureHasPermissionToManage(UserDomain actor, UserDomain target) {
        if (actor.isOwner()) {
            return;
        }

        if (!actor.getId().equals(target.getId())) {
            throw new BusinessRuleException(
                    "Você não tem permissão para gerenciar este usuário");
        }
    }

    public void ensureCanChangeRoles(UserDomain actor) {
        if (!actor.isOwner()) {
            throw new BusinessRuleException(
                    "Apenas proprietários podem alterar roles de usuários");
        }
    }
}