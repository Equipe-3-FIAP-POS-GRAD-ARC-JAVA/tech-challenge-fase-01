package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;

@DisplayName("SecurityUser Tests")
class SecurityUserTest {

    @Nested
    @DisplayName("Constructor Tests")
    class ConstructorTests {

        @Test
        @DisplayName("Should create SecurityUser with CLIENT role")
        void shouldCreateSecurityUserWithClientRole() {
            // Given
            UUID userId = UUID.randomUUID();
            String login = "testuser";
            String password = "password123";
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Test User", "test@example.com", 
                    login, password, null, null,
                    List.of(RolesEnum.CLIENT), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            assertNotNull(securityUser);
            assertEquals(userId, securityUser.getId());
            assertEquals(login, securityUser.getUsername());
            assertEquals(password, securityUser.getPassword());
            
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            assertTrue(authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT")));
        }

        @Test
        @DisplayName("Should create SecurityUser with ADMIN role")
        void shouldCreateSecurityUserWithAdminRole() {
            // Given
            UUID userId = UUID.randomUUID();
            String login = "adminuser";
            String password = "admin123";
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Admin User", "admin@example.com", 
                    login, password, null, null,
                    List.of(RolesEnum.ADMIN), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            assertNotNull(securityUser);
            assertEquals(userId, securityUser.getId());
            assertEquals(login, securityUser.getUsername());
            assertEquals(password, securityUser.getPassword());
            
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            assertTrue(authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        }

        @Test
        @DisplayName("Should create SecurityUser with OWNER role")
        void shouldCreateSecurityUserWithOwnerRole() {
            // Given
            UUID userId = UUID.randomUUID();
            String login = "owneruser";
            String password = "owner123";
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Owner User", "owner@example.com", 
                    login, password, null, null,
                    List.of(RolesEnum.OWNER), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            assertNotNull(securityUser);
            assertEquals(userId, securityUser.getId());
            assertEquals(login, securityUser.getUsername());
            assertEquals(password, securityUser.getPassword());
            
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            assertTrue(authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_OWNER")));
        }

        @Test
        @DisplayName("Should create SecurityUser with multiple roles")
        void shouldCreateSecurityUserWithMultipleRoles() {
            // Given
            UUID userId = UUID.randomUUID();
            String login = "multiuser";
            String password = "multi123";
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Multi User", "multi@example.com", 
                    login, password, null, null,
                    List.of(RolesEnum.CLIENT, RolesEnum.ADMIN), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            assertNotNull(securityUser);
            assertEquals(userId, securityUser.getId());
            assertEquals(login, securityUser.getUsername());
            assertEquals(password, securityUser.getPassword());
            
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(2, authorities.size());
            assertTrue(authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_CLIENT")));
            assertTrue(authorities.stream()
                    .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        }
    }

    @Nested
    @DisplayName("UserDetails Implementation Tests")
    class UserDetailsImplementationTests {

        private SecurityUser createSecurityUser() {
            UUID userId = UUID.randomUUID();
            JpaUserEntity userEntity = new JpaUserEntity(
                    userId, "Test User", "test@example.com", 
                    "testuser", "password123", null, null,
                    List.of(RolesEnum.CLIENT), true
            );
            return new SecurityUser(userEntity);
        }

        @Test
        @DisplayName("Should return true for isAccountNonExpired")
        void shouldReturnTrueForIsAccountNonExpired() {
            // Given
            SecurityUser securityUser = createSecurityUser();

            // When & Then
            assertTrue(securityUser.isAccountNonExpired());
        }

        @Test
        @DisplayName("Should return true for isAccountNonLocked")
        void shouldReturnTrueForIsAccountNonLocked() {
            // Given
            SecurityUser securityUser = createSecurityUser();

            // When & Then
            assertTrue(securityUser.isAccountNonLocked());
        }

        @Test
        @DisplayName("Should return true for isCredentialsNonExpired")
        void shouldReturnTrueForIsCredentialsNonExpired() {
            // Given
            SecurityUser securityUser = createSecurityUser();

            // When & Then
            assertTrue(securityUser.isCredentialsNonExpired());
        }

        @Test
        @DisplayName("Should return true for isEnabled")
        void shouldReturnTrueForIsEnabled() {
            // Given
            SecurityUser securityUser = createSecurityUser();

            // When & Then
            assertTrue(securityUser.isEnabled());
        }
    }

    @Nested
    @DisplayName("Authority Mapping Tests")
    class AuthorityMappingTests {

        @Test
        @DisplayName("Should correctly map CLIENT role to ROLE_CLIENT authority")
        void shouldCorrectlyMapClientRoleToRoleClientAuthority() {
            // Given
            JpaUserEntity userEntity = new JpaUserEntity(
                    UUID.randomUUID(), "Client User", "client@example.com", 
                    "clientuser", "pass", null, null,
                    List.of(RolesEnum.CLIENT), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            
            GrantedAuthority authority = authorities.iterator().next();
            assertEquals("ROLE_CLIENT", authority.getAuthority());
        }

        @Test
        @DisplayName("Should correctly map ADMIN role to ROLE_ADMIN authority")
        void shouldCorrectlyMapAdminRoleToRoleAdminAuthority() {
            // Given
            JpaUserEntity userEntity = new JpaUserEntity(
                    UUID.randomUUID(), "Admin User", "admin@example.com", 
                    "adminuser", "pass", null, null,
                    List.of(RolesEnum.ADMIN), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            
            GrantedAuthority authority = authorities.iterator().next();
            assertEquals("ROLE_ADMIN", authority.getAuthority());
        }

        @Test
        @DisplayName("Should correctly map OWNER role to ROLE_OWNER authority")
        void shouldCorrectlyMapOwnerRoleToRoleOwnerAuthority() {
            // Given
            JpaUserEntity userEntity = new JpaUserEntity(
                    UUID.randomUUID(), "Owner User", "owner@example.com", 
                    "owneruser", "pass", null, null,
                    List.of(RolesEnum.OWNER), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(1, authorities.size());
            
            GrantedAuthority authority = authorities.iterator().next();
            assertEquals("ROLE_OWNER", authority.getAuthority());
        }

        @Test
        @DisplayName("Should maintain order independence for multiple roles")
        void shouldMaintainOrderIndependenceForMultipleRoles() {
            // Given
            JpaUserEntity userEntity = new JpaUserEntity(
                    UUID.randomUUID(), "Multi User", "multi@example.com", 
                    "multiuser", "pass", null, null,
                    List.of(RolesEnum.ADMIN, RolesEnum.CLIENT, RolesEnum.OWNER), true
            );

            // When
            SecurityUser securityUser = new SecurityUser(userEntity);

            // Then
            Collection<? extends GrantedAuthority> authorities = securityUser.getAuthorities();
            assertEquals(3, authorities.size());
            
            // Verify all authorities exist regardless of order
            assertTrue(authorities.stream()
                    .anyMatch(auth -> "ROLE_CLIENT".equals(auth.getAuthority())));
            assertTrue(authorities.stream()
                    .anyMatch(auth -> "ROLE_ADMIN".equals(auth.getAuthority())));
            assertTrue(authorities.stream()
                    .anyMatch(auth -> "ROLE_OWNER".equals(auth.getAuthority())));
        }
    }
}