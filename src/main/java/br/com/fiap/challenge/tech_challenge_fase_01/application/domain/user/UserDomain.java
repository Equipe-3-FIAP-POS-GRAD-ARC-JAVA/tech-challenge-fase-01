package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import lombok.Builder;
import lombok.Getter;

/**
 * Entidade de Domínio Rica - User
 * 
 * Esta classe representa o conceito de Usuário no domínio da aplicação.
 * Seguindo os princípios de Domain-Driven Design (DDD):
 * - Encapsula regras de negócio
 * - Valida seu próprio estado
 * - Fornece métodos de fábrica para criação
 * - Protege invariantes do domínio
 * - Usa Value Objects para conceitos do domínio (Email, Username, PersonName)
 */
@Getter
@Builder
public class UserDomain {

    // Constantes de validação mantidas para senha (não é Value Object ainda)
    private static final int PASSWORD_MIN_LENGTH = 6;
    private static final int PASSWORD_MAX_LENGTH = 100;

    private String id;
    private PersonName name; // Value Object
    private Email email; // Value Object
    private Username login; // Value Object
    private String password; // Mantido como String (será criptografado)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<RolesEnum> role;
    private boolean isActive;

    // Construtor privado para forçar uso dos factory methods
    private UserDomain(String id, PersonName name, Email email, Username login,
            String password, LocalDateTime createdAt, LocalDateTime updatedAt,
            List<RolesEnum> role, boolean isActive) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.login = login;
        this.password = password;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.role = role;
        this.isActive = isActive;
    }

    // Getters convenientes que retornam String (compatibilidade)

    /**
     * Retorna o nome como String.
     */
    public String getName() {
        return name != null ? name.getValue() : null;
    }

    /**
     * Retorna o email como String.
     */
    public String getEmail() {
        return email != null ? email.getValue() : null;
    }

    /**
     * Retorna o login como String.
     */
    public String getLogin() {
        return login != null ? login.getValue() : null;
    }

    // Getters dos Value Objects (para uso interno do domínio)

    /**
     * Retorna o Value Object PersonName.
     */
    public PersonName getPersonName() {
        return name;
    }

    /**
     * Retorna o Value Object Email.
     */
    public Email getEmailObject() {
        return email;
    }

    /**
     * Retorna o Value Object Username.
     */
    public Username getUsernameObject() {
        return login;
    }

    /**
     * Cria um novo usuário cliente.
     * Aplica as regras de negócio para criação de cliente.
     */
    public static UserDomain createClient(String name, String email, String login, String password) {
        validatePassword(password);

        return UserDomain.builder()
                .name(PersonName.of(name))
                .email(Email.of(email))
                .login(Username.of(login))
                .password(password) // Senha já deve vir criptografada
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.CLIENT))
                .build();
    }

    /**
     * Cria um novo usuário proprietário (owner).
     * Aplica as regras de negócio para criação de proprietário.
     */
    public static UserDomain createOwner(String name, String email, String login, String password) {
        validatePassword(password);

        return UserDomain.builder()
                .name(PersonName.of(name))
                .email(Email.of(email))
                .login(Username.of(login))
                .password(password) // Senha já deve vir criptografada
                .createdAt(LocalDateTime.now())
                .isActive(true)
                .role(List.of(RolesEnum.OWNER))
                .build();
    }

    /**
     * Atualiza as informações básicas do usuário.
     * Valida os novos dados antes de aplicar.
     * 
     * Regra de negócio: Usuário inativo não pode ter suas informações atualizadas.
     */
    public void updateInfo(String name, String email, String login) {
        if (!this.isActive) {
            throw new BusinessRuleException("Não é possível atualizar informações de usuário inativo");
        }

        // Value Objects fazem validação automaticamente
        this.name = PersonName.of(name);
        this.email = Email.of(email);
        this.login = Username.of(login);
        this.updatedAt = LocalDateTime.now();
    }

    public void changePassword(String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new InvalidFieldException("password", "Nova senha não pode ser vazia");
        }

        // Validação adicional: não permitir senha igual à atual
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

    // Métodos de Validação (Invariantes do Domínio)

    /**
     * Validações de name, email e login foram movidas para os Value Objects:
     * - PersonName.of(name)
     * - Email.of(email)
     * - Username.of(login)
     * 
     * Apenas a validação de senha permanece aqui pois não é Value Object.
     */

    /**
     * Valida a senha do usuário.
     * 
     * Regras de negócio:
     * - Não pode ser nula ou vazia
     * - Deve ter entre 6 e 100 caracteres
     * - Deve conter pelo menos uma letra E um número
     * 
     * Nota: A senha deve chegar já criptografada nos Use Cases.
     * Esta validação é para a senha em texto plano antes da criptografia.
     */
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

        // Validação de complexidade (regra de negócio)
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

    // Métodos auxiliares para Lombok Builder

    /**
     * Classe Builder customizada para garantir validações.
     */
    public static class UserDomainBuilder {
        // Lombok gera automaticamente os métodos do builder
        // Podemos adicionar métodos customizados aqui se necessário
    }

    // Sobrescrita de equals e hashCode baseado em ID (Entity pattern)

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
        return "UserDomain{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", isActive=" + isActive +
                '}';
    }
}
