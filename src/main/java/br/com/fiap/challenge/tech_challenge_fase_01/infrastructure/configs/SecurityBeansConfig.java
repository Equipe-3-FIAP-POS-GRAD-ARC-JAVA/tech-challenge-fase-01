package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.auth.AuthPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.JwtTokenPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.service.auth.AuthUseCases;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateOwnerUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByNameUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdatePasswordUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdateUserUseCase;

/**
 * Configuração de beans da camada de aplicação e segurança.
 * 
 * Responsabilidades (SOLID - SRP):
 * - Instanciar e configurar Use Cases
 * - Resolver dependências entre Use Cases e Ports
 * - Configurar beans de segurança (PasswordEncoder, AuthenticationManager)
 * 
 * Arquitetura Hexagonal:
 * - Configuração da camada de aplicação
 * - Liga Ports com suas implementações (Adapters)
 * - Mantém dependências explícitas e testáveis
 */
@Configuration
public class SecurityBeansConfig {

    /**
     * Bean do Spring Security PasswordEncoder.
     * Usa BCrypt com fator de custo padrão (10).
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean do AuthenticationManager do Spring Security.
     * Necessário para autenticação via username/password.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean do AuthenticationProvider.
     * Configura como o Spring Security deve autenticar usuários.
     */
    @Bean
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }

    @Bean
    public UserDomainService userDomainService(UserRepositoryPort userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public UserCreatePort userCreatePort(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateUserUseCase(userRepository, userDomainService, passwordEncoder);
    }

    @Bean
    public UserCreateOwnerPort userCreateOwnerPort(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateOwnerUseCase(userRepository, userDomainService, passwordEncoder);
    }

    @Bean
    public UserDeletePort userDeletePort(UserRepositoryPort userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public UserUpdatePort userUpdatePort(UserRepositoryPort userRepository) {
        return new UpdateUserUseCase(userRepository);
    }

    @Bean
    public UserUpdatePasswordPort userUpdatePasswordPort(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder) {
        return new UpdatePasswordUseCase(userRepository, passwordEncoder);
    }

    @Bean
    public UserFindByNamePort userFindByNamePort(UserRepositoryPort userRepository) {
        return new FindUserByNameUseCase(userRepository);
    }

    @Bean
    public UserFindByIdPort userFindByIdPort(UserRepositoryPort userRepository) {
        return new FindUserByIdUseCase(userRepository);
    }

    /**
     * Bean do Use Case de autenticação.
     * Responsável por validar credenciais e gerar token JWT.
     */
    @Bean
    public AuthPort authPort(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder,
            JwtTokenPort jwtTokenPort) {
        return new AuthUseCases(userRepository, passwordEncoder, jwtTokenPort);
    }
}
