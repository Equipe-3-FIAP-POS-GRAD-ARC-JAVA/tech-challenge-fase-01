package br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.BusinessRuleException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.exception.InvalidFieldException;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.PersonName;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Username;

@DisplayName("UserDomain Tests")
class UserDomainTest {

    @Nested
    @DisplayName("Client Creation Tests")
    class ClientCreationTests {

        @Test
        @DisplayName("Should create client user with valid data")
        void shouldCreateClientUserWithValidData() {
            // Given
            String name = "João Silva";
            String email = "joao@example.com";
            String login = "joaosilva";
            String password = "password123";

            // When
            UserDomain user = UserDomain.createClient(name, email, login, password);

            // Then
            assertNotNull(user);
            assertEquals(name, user.getName());
            assertEquals(email, user.getEmail());
            assertEquals(login, user.getLogin());
            // Password is private, so we can't directly test it
            assertTrue(user.isActive());
            assertTrue(user.isClient());
            assertFalse(user.isOwner());
            assertTrue(user.hasRole(RolesEnum.CLIENT));
            // CreatedAt is set during creation
        }

        @Test
        @DisplayName("Should throw exception when creating client with invalid password")
        void shouldThrowExceptionWhenCreatingClientWithInvalidPassword() {
            // Given
            String name = "João Silva";
            String email = "joao@example.com";
            String login = "joaosilva";
            String invalidPassword = "123456";

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> UserDomain.createClient(name, email, login, invalidPassword)
            );
            
            assertTrue(exception.getMessage().contains("Senha deve conter pelo menos uma letra"));
        }

        @Test
        @DisplayName("Should throw exception when creating client with short password")
        void shouldThrowExceptionWhenCreatingClientWithShortPassword() {
            // Given
            String name = "João Silva";
            String email = "joao@example.com";
            String login = "joaosilva";
            String shortPassword = "abc1"; // Muito curta

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> UserDomain.createClient(name, email, login, shortPassword)
            );
            
            assertTrue(exception.getMessage().contains("Senha deve ter no mínimo 6 caracteres"));
        }

        @Test
        @DisplayName("Should throw exception when creating client with null password")
        void shouldThrowExceptionWhenCreatingClientWithNullPassword() {
            // Given
            String name = "João Silva";
            String email = "joao@example.com";
            String login = "joaosilva";

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> UserDomain.createClient(name, email, login, null)
            );
            
            assertTrue(exception.getMessage().contains("Senha é obrigatória"));
        }
    }

    @Nested
    @DisplayName("Owner Creation Tests")
    class OwnerCreationTests {

        @Test
        @DisplayName("Should create owner user with valid data")
        void shouldCreateOwnerUserWithValidData() {
            // Given
            String name = "Maria Proprietária";
            String email = "maria@restaurant.com";
            String login = "mariaowner";
            String password = "ownerpass123";

            // When
            UserDomain user = UserDomain.createOwner(name, email, login, password);

            // Then
            assertNotNull(user);
            assertEquals(name, user.getName());
            assertEquals(email, user.getEmail());
            assertEquals(login, user.getLogin());
            // Password is private, so we can't directly test it
            assertTrue(user.isActive());
            assertFalse(user.isClient());
            assertTrue(user.isOwner());
            assertTrue(user.hasRole(RolesEnum.OWNER));
            // CreatedAt is set during creation
        }

        @Test
        @DisplayName("Should throw exception when creating owner with password without digits")
        void shouldThrowExceptionWhenCreatingOwnerWithPasswordWithoutDigits() {
            // Given
            String name = "Maria Proprietária";
            String email = "maria@restaurant.com";
            String login = "mariaowner";
            String passwordWithoutDigits = "onlyletters";

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> UserDomain.createOwner(name, email, login, passwordWithoutDigits)
            );
            
            assertTrue(exception.getMessage().contains("Senha deve conter pelo menos um número"));
        }
    }

    @Nested
    @DisplayName("Update Information Tests")
    class UpdateInformationTests {

        @Test
        @DisplayName("Should update user information when user is active")
        void shouldUpdateUserInformationWhenUserIsActive() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@old.com", "joaoold", "password123");
            String newName = "João Silva Atualizado";
            String newEmail = "joao.new@example.com";
            String newLogin = "joaonew";

            // When
            user.updateInfo(newName, newEmail, newLogin);

            // Then
            assertEquals(newName, user.getName());
            assertEquals(newEmail, user.getEmail());
            assertEquals(newLogin, user.getLogin());
            // UpdatedAt is set during update
        }

        @Test
        @DisplayName("Should throw exception when updating information of inactive user")
        void shouldThrowExceptionWhenUpdatingInformationOfInactiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            user.deactivate();

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> user.updateInfo("Novo Nome", "novo@email.com", "novologin")
            );
            
            assertTrue(exception.getMessage().contains("Não é possível atualizar informações de usuário inativo"));
        }
    }

    @Nested
    @DisplayName("Password Change Tests")
    class PasswordChangeTests {

        @Test
        @DisplayName("Should change password successfully")
        void shouldChangePasswordSuccessfully() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            String newPassword = "newpassword456";

            // When
            user.changePassword(newPassword);

            // Then
            assertEquals(newPassword, user.getPassword());
            assertNotNull(user.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when new password is null")
        void shouldThrowExceptionWhenNewPasswordIsNull() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> user.changePassword(null)
            );
            
            assertTrue(exception.getMessage().contains("Nova senha não pode ser vazia"));
        }

        @Test
        @DisplayName("Should throw exception when new password is blank")
        void shouldThrowExceptionWhenNewPasswordIsBlank() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> user.changePassword("   ")
            );
            
            assertTrue(exception.getMessage().contains("Nova senha não pode ser vazia"));
        }

        @Test
        @DisplayName("Should throw exception when new password equals current password")
        void shouldThrowExceptionWhenNewPasswordEqualsCurrentPassword() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> user.changePassword("password123")
            );
            
            assertTrue(exception.getMessage().contains("A nova senha deve ser diferente da senha atual"));
        }
    }

    @Nested
    @DisplayName("User Activation Tests")
    class UserActivationTests {

        @Test
        @DisplayName("Should deactivate active user")
        void shouldDeactivateActiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            assertTrue(user.isActive());

            // When
            user.deactivate();

            // Then
            assertFalse(user.isActive());
            assertNotNull(user.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when deactivating already inactive user")
        void shouldThrowExceptionWhenDeactivatingAlreadyInactiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            user.deactivate();

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> user.deactivate()
            );
            
            assertTrue(exception.getMessage().contains("Usuário já está inativo"));
        }

        @Test
        @DisplayName("Should activate inactive user")
        void shouldActivateInactiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            user.deactivate();
            assertFalse(user.isActive());

            // When
            user.activate();

            // Then
            assertTrue(user.isActive());
            assertNotNull(user.getUpdatedAt());
        }

        @Test
        @DisplayName("Should throw exception when activating already active user")
        void shouldThrowExceptionWhenActivatingAlreadyActiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> user.activate()
            );
            
            assertTrue(exception.getMessage().contains("Usuário já está ativo"));
        }
    }

    @Nested
    @DisplayName("Role Management Tests")
    class RoleManagementTests {

        @Test
        @DisplayName("Should check if user has specific role")
        void shouldCheckIfUserHasSpecificRole() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            UserDomain owner = UserDomain.createOwner("Maria", "maria@example.com", "maria", "password123");

            // When & Then
            assertTrue(client.hasRole(RolesEnum.CLIENT));
            assertFalse(client.hasRole(RolesEnum.OWNER));
            assertFalse(client.hasRole(RolesEnum.ADMIN));

            assertTrue(owner.hasRole(RolesEnum.OWNER));
            assertFalse(owner.hasRole(RolesEnum.CLIENT));
            assertFalse(owner.hasRole(RolesEnum.ADMIN));
        }

        @Test
        @DisplayName("Should identify client users correctly")
        void shouldIdentifyClientUsersCorrectly() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            UserDomain owner = UserDomain.createOwner("Maria", "maria@example.com", "maria", "password123");

            // When & Then
            assertTrue(client.isClient());
            assertFalse(owner.isClient());
        }

        @Test
        @DisplayName("Should identify owner users correctly")
        void shouldIdentifyOwnerUsersCorrectly() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            UserDomain owner = UserDomain.createOwner("Maria", "maria@example.com", "maria", "password123");

            // When & Then
            assertFalse(client.isOwner());
            assertTrue(owner.isOwner());
        }
    }

    @Nested
    @DisplayName("Business Rule Validation Tests")
    class BusinessRuleValidationTests {

        @Test
        @DisplayName("Should pass ensureIsActive when user is active")
        void shouldPassEnsureIsActiveWhenUserIsActive() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then - Should not throw exception
            user.ensureIsActive();
        }

        @Test
        @DisplayName("Should throw exception when ensureIsActive called on inactive user")
        void shouldThrowExceptionWhenEnsureIsActiveCalledOnInactiveUser() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            user.deactivate();

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> user.ensureIsActive()
            );
            
            assertTrue(exception.getMessage().contains("Operação não permitida: usuário está inativo"));
        }

        @Test
        @DisplayName("Should pass ensureIsOwner when user is owner")
        void shouldPassEnsureIsOwnerWhenUserIsOwner() {
            // Given
            UserDomain owner = UserDomain.createOwner("Maria", "maria@example.com", "maria", "password123");

            // When & Then - Should not throw exception
            owner.ensureIsOwner();
        }

        @Test
        @DisplayName("Should throw exception when ensureIsOwner called on client")
        void shouldThrowExceptionWhenEnsureIsOwnerCalledOnClient() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> client.ensureIsOwner()
            );
            
            assertTrue(exception.getMessage().contains("Operação permitida apenas para proprietários"));
        }

        @Test
        @DisplayName("Should pass ensureHasRole when user has required role")
        void shouldPassEnsureHasRoleWhenUserHasRequiredRole() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then - Should not throw exception
            client.ensureHasRole(RolesEnum.CLIENT);
        }

        @Test
        @DisplayName("Should throw exception when ensureHasRole called with role user doesn't have")
        void shouldThrowExceptionWhenEnsureHasRoleCalledWithRoleUserDoesntHave() {
            // Given
            UserDomain client = UserDomain.createClient("João", "joao@example.com", "joao", "password123");

            // When & Then
            BusinessRuleException exception = assertThrows(
                    BusinessRuleException.class,
                    () -> client.ensureHasRole(RolesEnum.ADMIN)
            );
            
            assertTrue(exception.getMessage().contains("Operação requer a role: ADMIN"));
        }
    }

    @Nested
    @DisplayName("Password Validation Tests")
    class PasswordValidationTests {

        @Test
        @DisplayName("Should create user with password having minimum required length")
        void shouldCreateUserWithPasswordHavingMinimumRequiredLength() {
            // Given
            String validPassword = "abc123"; // 6 caracteres, mínimo

            // When & Then - Should not throw exception
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", validPassword);
            assertNotNull(user);
        }

        @Test
        @DisplayName("Should throw exception when password is too long")
        void shouldThrowExceptionWhenPasswordIsTooLong() {
            // Given
            StringBuilder longPassword = new StringBuilder();
            for (int i = 0; i < 51; i++) {
                longPassword.append("ab"); // 102 caracteres
            }

            // When & Then
            InvalidFieldException exception = assertThrows(
                    InvalidFieldException.class,
                    () -> UserDomain.createClient("João", "joao@example.com", "joao", longPassword.toString())
            );
            
            assertTrue(exception.getMessage().contains("Senha deve ter no máximo 100 caracteres"));
        }

        @Test
        @DisplayName("Should accept password with special characters")
        void shouldAcceptPasswordWithSpecialCharacters() {
            // Given
            String passwordWithSpecial = "pass@123!";

            // When & Then - Should not throw exception
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", passwordWithSpecial);
            assertNotNull(user);
        }
    }

    @Nested
    @DisplayName("Object Methods Tests")
    class ObjectMethodsTests {

        @Test
        @DisplayName("Should have proper equals implementation")
        void shouldHaveProperEqualsImplementation() {
            // Given
            UUID id = UUID.randomUUID();
            UserDomain user1 = UserDomain.builder()
                    .id(id)
                    .name(PersonName.of("João"))
                    .email(Email.of("joao@example.com"))
                    .login(Username.of("joao"))
                    .password("password123")
                    .role(List.of(RolesEnum.CLIENT))
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            UserDomain user2 = UserDomain.builder()
                    .id(id)
                    .name(PersonName.of("Maria"))
                    .email(Email.of("maria@example.com"))
                    .login(Username.of("maria"))
                    .password("password456")
                    .role(List.of(RolesEnum.OWNER))
                    .isActive(true)
                    .createdAt(LocalDateTime.now())
                    .build();

            // When & Then
            assertEquals(user1, user2); // Same ID, should be equal
            assertEquals(user1.hashCode(), user2.hashCode());
        }

        @Test
        @DisplayName("Should have proper toString implementation")
        void shouldHaveProperToStringImplementation() {
            // Given
            UserDomain user = UserDomain.createClient("João Silva", "joao@example.com", "joaosilva", "password123");

            // When
            String toString = user.toString();

            // Then
            assertNotNull(toString);
            assertTrue(toString.contains("UserDomain"));
            assertTrue(toString.contains("João Silva"));
            assertTrue(toString.contains("joao@example.com"));
            assertTrue(toString.contains("joaosilva"));
        }

        @Test
        @DisplayName("Should get ID as string correctly")
        void shouldGetIdAsStringCorrectly() {
            // Given
            UserDomain user = UserDomain.createClient("João", "joao@example.com", "joao", "password123");
            
            // When
            String idString = user.getIdAsString();

            // Then
            if (user.getId() != null) {
                assertEquals(user.getId().toString(), idString);
            } else {
                assertEquals(null, idString);
            }
        }

        @Test
        @DisplayName("Should get value object accessors correctly")
        void shouldGetValueObjectAccessorsCorrectly() {
            // Given
            UserDomain user = UserDomain.createClient("João Silva", "joao@example.com", "joaosilva", "password123");

            // When & Then
            assertNotNull(user.getPersonName());
            assertNotNull(user.getEmailObject());
            assertNotNull(user.getUsernameObject());
            
            assertEquals("João Silva", user.getPersonName().getValue());
            assertEquals("joao@example.com", user.getEmailObject().getValue());
            assertEquals("joaosilva", user.getUsernameObject().getValue());
        }
    }
}