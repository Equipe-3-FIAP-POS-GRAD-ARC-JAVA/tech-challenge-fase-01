package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserDomain {

    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 100;

    private UUID id;
    private PersonName name;
    private Email email;
    private Username login;
    private String password;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RolesEnum> role;
    private boolean isActive;

    public String getIdAsString() {
        return id != null ? id.toString() : null;
    }

    public UUID getId() {
        return id != null ? id : null;
    }

    public String getName() {
        return name != null ? name.getValue() : null;
    }

    public String getEmail() {
        return email != null ? email.getValue() : null;
    }

    public String getLogin() {
        return login != null ? login.getValue() : null;
    }

    public PersonName getPersonName() {
        return name;
    }

    public Email getEmailObject() {
        return email;
    }

    public Username getUsernameObject() {
        return login;
    }

    public static UserDomain createClient(String name, String email, String login, String password) {
        validatePassword(password);

        return UserDomain.builder()
                .name(PersonName.of(name))
                .email(Email.of(email))
                .login(Username.of(login))
                .password(password)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.CLIENT))
                .build();
    }

    public static UserDomain createOwner(String name, String email, String login, String password) {
        validatePassword(password);

        return UserDomain.builder()
                .name(PersonName.of(name))
                .email(Email.of(email))
                .login(Username.of(login))
                .password(password)
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.OWNER))
                .build();
    }

    public void updateInfo(String name, String email, String login) {
        if (!this.isActive) {
            throw new BusinessRuleException("Não é possível atualizar informações de usuário inativo");
        }

        this.name = PersonName.of(name);
        this.email = Email.of(email);
        this.login = Username.of(login);
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidFieldException("password", "Nova senha não pode ser vazia");
        }

        if (this.password != null && this.password.equals(newPassword)) {
            throw new BusinessRuleException("A nova senha deve ser diferente da senha atual");
        }

        this.password = newPassword;
        this.updatedAt = LocalDateTime.now();
    }

    public void deactivate() {
        if (!this.isActive) {
            throw new BusinessRuleException("Usuário já está inativo");
        }

        this.isActive = false;
        this.updatedAt = LocalDateTime.now();
    }

    public void activate() {
        if (this.isActive) {
            throw new BusinessRuleException("Usuário já está ativo");
        }

        this.isActive = true;
        this.updatedAt = LocalDateTime.now();
    }

    public boolean hasRole(RolesEnum role) {
        return this.role != null && this.role.contains(role);
    }

    public boolean isClient() {
        return hasRole(RolesEnum.CLIENT);
    }

    public boolean isOwner() {
        return hasRole(RolesEnum.OWNER);
    }

    public boolean isActive() {
        return this.isActive;
    }

    public void ensureIsActive() {
        if (!this.isActive) {
            throw new BusinessRuleException("Operação não permitida: usuário está inativo");
        }
    }

    public void ensureIsOwner() {
        if (!isOwner()) {
            throw new BusinessRuleException("Operação permitida apenas para proprietários");
        }
    }

    public void ensureHasRole(RolesEnum requiredRole) {
        if (!hasRole(requiredRole)) {
            throw new BusinessRuleException(
                    String.format("Operação requer a role: %s", requiredRole));
        }
    }

    private static void validatePassword(String password) {
        if (password == null || password.isBlank()) {
            throw new InvalidFieldException("password", "Senha é obrigatória");
        }

        if (password.length() < PASSWORD_MIN_LENGTH) {
            throw new InvalidFieldException("password",
                    String.format("Senha deve ter no mínimo %d caracteres", PASSWORD_MIN_LENGTH));
        }

        if (password.length() > PASSWORD_MAX_LENGTH) {
            throw new InvalidFieldException("password",
                    String.format("Senha deve ter no máximo %d caracteres", PASSWORD_MAX_LENGTH));
        }

        boolean hasLetter = password.matches(".*[a-zA-Z].*");
        boolean hasDigit = password.matches(".*\\d.*");

        if (!hasLetter) {
            throw new InvalidFieldException("password",
                    "Senha deve conter pelo menos uma letra");
        }

        if (!hasDigit) {
            throw new InvalidFieldException("password",
                    "Senha deve conter pelo menos um número");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDomain that = (UserDomain) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDomain{"
                + "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}