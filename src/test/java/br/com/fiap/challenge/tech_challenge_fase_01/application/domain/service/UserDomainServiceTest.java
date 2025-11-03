package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;
import br.com.fiap.challenge.tech_challenge_fase_01.application.exception.UserAlreadyExistsException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;

@ExtendWith(MockitoExtension.class)
@DisplayName("UserDomainService Tests")
class UserDomainServiceTest {

    @Mock
    private UserRepositoryPort userRepository;

    @InjectMocks
    private UserDomainService userDomainService;

    private UserDomain createValidUser(String name, String username, String email) {
        UserDomain user = UserDomain.createClient(name, email, username, "password123");
        // Usar reflection para definir o ID
        try {
            java.lang.reflect.Field idField = UserDomain.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(user, java.util.UUID.randomUUID());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID no usuário de teste", e);
        }
        return user;
    }

    private UserDomain createOwnerUser() {
        UserDomain owner = UserDomain.createOwner("Owner User", "owner@test.com", "owner", "password123");
        // Usar reflection para definir o ID
        try {
            java.lang.reflect.Field idField = UserDomain.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(owner, java.util.UUID.randomUUID());
        } catch (Exception e) {
            throw new RuntimeException("Erro ao definir ID no usuário owner de teste", e);
        }
        return owner;
    }

    @Nested
    @DisplayName("Username Uniqueness Tests")
    class UsernameUniquenessTests {

        @Test
        @DisplayName("Should pass when username is unique")
        void shouldPassWhenUsernameIsUnique() {
            // Given
            Username username = Username.of("newuser");
            when(userRepository.existsByUsername(username.getValue())).thenReturn(false);

            // When & Then
            // Should not throw any exception
            userDomainService.ensureUsernameIsUnique(username);
            
            verify(userRepository).existsByUsername(username.getValue());
        }

        @Test
        @DisplayName("Should throw exception when username already exists")
        void shouldThrowExceptionWhenUsernameExists() {
            // Given
            Username username = Username.of("existinguser");
            when(userRepository.existsByUsername(username.getValue())).thenReturn(true);

            // When & Then
            UserAlreadyExistsException exception = assertThrows(
                    UserAlreadyExistsException.class,
                    () -> userDomainService.ensureUsernameIsUnique(username)
            );
            
            assertEquals("Já existe um usuário com o login: existinguser", exception.getMessage());
            verify(userRepository).existsByUsername(username.getValue());
        }

        @Test
        @DisplayName("Should pass when username string is unique")
        void shouldPassWhenUsernameStringIsUnique() {
            // Given
            String username = "newuser";
            when(userRepository.existsByUsername(username)).thenReturn(false);

            // When & Then
            userDomainService.ensureUsernameIsUnique(username);
            
            verify(userRepository).existsByUsername(username);
        }

        @Test
        @DisplayName("Should throw exception when username string already exists")
        void shouldThrowExceptionWhenUsernameStringExists() {
            // Given
            String username = "existinguser";
            when(userRepository.existsByUsername(username)).thenReturn(true);

            // When & Then
            UserAlreadyExistsException exception = assertThrows(
                    UserAlreadyExistsException.class,
                    () -> userDomainService.ensureUsernameIsUnique(username)
            );
            
            assertEquals("Já existe um usuário com o login: existinguser", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Email Uniqueness Tests")
    class EmailUniquenessTests {

        @Test
        @DisplayName("Should pass when email is unique")
        void shouldPassWhenEmailIsUnique() {
            // Given
            Email email = Email.of("new@test.com");
            when(userRepository.findByEmail(email.getValue())).thenReturn(Optional.empty());

            // When & Then
            userDomainService.ensureEmailIsUnique(email);
            
            verify(userRepository).findByEmail(email.getValue());
        }

        @Test
        @DisplayName("Should throw exception when email already exists")
        void shouldThrowExceptionWhenEmailExists() {
            // Given
            Email email = Email.of("existing@test.com");
            UserDomain existingUser = createValidUser("Existing User", "existing", "existing@test.com");
            when(userRepository.findByEmail(email.getValue())).thenReturn(Optional.of(existingUser));

            // When & Then
            UserAlreadyExistsException exception = assertThrows(
                    UserAlreadyExistsException.class,
                    () -> userDomainService.ensureEmailIsUnique(email)
            );
            
            assertEquals("Já existe um usuário com o email: existing@test.com", exception.getMessage());
            verify(userRepository).findByEmail(email.getValue());
        }
    }

    @Nested
    @DisplayName("User Deactivation Tests")
    class UserDeactivationTests {

        @Test
        @DisplayName("Should pass when user is active and can be deactivated")
        void shouldPassWhenUserCanBeDeactivated() {
            // Given
            UserDomain activeUser = createValidUser("Active User", "activeuser", "active@test.com");

            // When & Then
            userDomainService.ensureCanBeDeactivated(activeUser);
        }

        @Test
        @DisplayName("Should throw exception when user is already inactive")
        void shouldThrowExceptionWhenUserAlreadyInactive() {
            // Given
            UserDomain inactiveUser = createValidUser("Inactive User", "inactiveuser", "inactive@test.com");
            inactiveUser.deactivate();

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> userDomainService.ensureCanBeDeactivated(inactiveUser)
            );
            
            assertEquals("Usuário já está inativo", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("User Deletion Tests")
    class UserDeletionTests {

        @Test
        @DisplayName("Should pass when user is inactive and can be deleted")
        void shouldPassWhenUserCanBeDeleted() {
            // Given
            UserDomain inactiveUser = createValidUser("Inactive User", "inactiveuser", "inactive@test.com");
            inactiveUser.deactivate();

            // When & Then
            userDomainService.ensureCanBeDeleted(inactiveUser);
        }

        @Test
        @DisplayName("Should throw exception when user is still active")
        void shouldThrowExceptionWhenUserStillActive() {
            // Given
            UserDomain activeUser = createValidUser("Active User", "activeuser", "active@test.com");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> userDomainService.ensureCanBeDeleted(activeUser)
            );
            
            assertEquals("Apenas usuários inativos podem ser excluídos. Desative o usuário primeiro.", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("User Uniqueness Tests")
    class UserUniquenessTests {

        @Test
        @DisplayName("Should pass when both username and email are unique")
        void shouldPassWhenBothUsernameAndEmailAreUnique() {
            // Given
            String username = "newuser";
            String email = "new@test.com";
            when(userRepository.existsByUsername(username)).thenReturn(false);

            // When & Then
            userDomainService.ensureUserIsUnique(username, email);
            
            verify(userRepository).existsByUsername(username);
        }

        @Test
        @DisplayName("Should throw exception when username exists")
        void shouldThrowExceptionWhenUsernameExistsInUniqueness() {
            // Given
            String username = "existinguser";
            String email = "new@test.com";
            when(userRepository.existsByUsername(username)).thenReturn(true);

            // When & Then
            UserAlreadyExistsException exception = assertThrows(
                    UserAlreadyExistsException.class,
                    () -> userDomainService.ensureUserIsUnique(username, email)
            );
            
            assertEquals("Já existe um usuário com o login: existinguser", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Permission Management Tests")
    class PermissionManagementTests {

        @Test
        @DisplayName("Should pass when owner manages any user")
        void shouldPassWhenOwnerManagesAnyUser() {
            // Given
            UserDomain owner = createOwnerUser();
            UserDomain targetUser = createValidUser("Target User", "target", "target@test.com");

            // When & Then
            userDomainService.ensureHasPermissionToManage(owner, targetUser);
        }

        @Test
        @DisplayName("Should pass when user manages themselves")
        void shouldPassWhenUserManagesThemselves() {
            // Given
            UserDomain user = createValidUser("User", "user", "user@test.com");

            // When & Then
            userDomainService.ensureHasPermissionToManage(user, user);
        }

        @Test
        @DisplayName("Should throw exception when non-owner tries to manage different user")
        void shouldThrowExceptionWhenNonOwnerTriesToManageDifferentUser() {
            // Given
            UserDomain actor = createValidUser("Actor User", "actor", "actor@test.com");
            UserDomain target = createValidUser("Target User", "target", "target@test.com");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> userDomainService.ensureHasPermissionToManage(actor, target)
            );
            
            assertEquals("Você não tem permissão para gerenciar este usuário", exception.getMessage());
        }
    }

    @Nested
    @DisplayName("Role Change Tests")
    class RoleChangeTests {

        @Test
        @DisplayName("Should pass when owner tries to change roles")
        void shouldPassWhenOwnerTriesToChangeRoles() {
            // Given
            UserDomain owner = createOwnerUser();

            // When & Then
            userDomainService.ensureCanChangeRoles(owner);
        }

        @Test
        @DisplayName("Should throw exception when non-owner tries to change roles")
        void shouldThrowExceptionWhenNonOwnerTriesToChangeRoles() {
            // Given
            UserDomain user = createValidUser("Regular User", "user", "user@test.com");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> userDomainService.ensureCanChangeRoles(user)
            );
            
            assertEquals("Apenas proprietários podem alterar roles de usuários", exception.getMessage());
        }
    }
}