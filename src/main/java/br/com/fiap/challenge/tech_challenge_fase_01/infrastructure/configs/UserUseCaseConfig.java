package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreateOwnerPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserDeletePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByIdPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserFindByNamePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePasswordPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserUpdatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateOwnerUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByNameUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdatePasswordUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdateUserUseCase;

/**
 * Configuração de beans dos Use Cases de usuário.
 *
 * Responsabilidades (SOLID - SRP):
 * - Instanciar e configurar Use Cases relacionados a usuários
 * - Resolver dependências entre Use Cases de usuário e Ports
 *
 * Arquitetura Hexagonal:
 * - Configuração da camada de aplicação para operações de usuário
 * - Liga Ports inbound de usuário com suas implementações (Use Cases)
 * - Mantém dependências explícitas e testáveis
 */
@Configuration
public class UserUseCaseConfig {

    /**
     * Bean do serviço de domínio de usuário.
     * Contém lógica de negócio reutilizável para usuários.
     *
     * @param userRepository porta para acesso ao repositório de usuários
     * @return UserDomainService configurado
     */
    @Bean
    public UserDomainService userDomainService(UserRepositoryPort userRepository) {
        return new UserDomainService(userRepository);
    }

    /**
     * Bean do Use Case para criação de usuários comuns.
     * Implementa a lógica de criação de usuários do sistema.
     *
     * @param userRepository porta para acesso ao repositório
     * @param userDomainService serviço de domínio para validações
     * @param passwordEncoder porta para codificação de senhas
     * @return implementação do UserCreatePort
     */
    @Bean
    public UserCreatePort userCreatePort(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateUserUseCase(userRepository, userDomainService, passwordEncoder);
    }

    /**
     * Bean do Use Case para criação de usuários proprietários.
     * Implementa a lógica de criação de usuários com perfil de proprietário.
     *
     * @param userRepository porta para acesso ao repositório
     * @param userDomainService serviço de domínio para validações
     * @param passwordEncoder porta para codificação de senhas
     * @return implementação do UserCreateOwnerPort
     */
    @Bean
    public UserCreateOwnerPort userCreateOwnerPort(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateOwnerUseCase(userRepository, userDomainService, passwordEncoder);
    }

    /**
     * Bean do Use Case para exclusão de usuários.
     * Implementa a lógica de remoção de usuários do sistema.
     *
     * @param userRepository porta para acesso ao repositório
     * @return implementação do UserDeletePort
     */
    @Bean
    public UserDeletePort userDeletePort(UserRepositoryPort userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    /**
     * Bean do Use Case para atualização de usuários.
     * Implementa a lógica de atualização dos dados do usuário.
     *
     * @param userRepository porta para acesso ao repositório
     * @return implementação do UserUpdatePort
     */
    @Bean
    public UserUpdatePort userUpdatePort(UserRepositoryPort userRepository) {
        return new UpdateUserUseCase(userRepository);
    }

    /**
     * Bean do Use Case para atualização de senha do usuário.
     * Implementa a lógica de alteração de senha com validações.
     *
     * @param userRepository porta para acesso ao repositório
     * @param passwordEncoder porta para codificação de senhas
     * @return implementação do UserUpdatePasswordPort
     */
    @Bean
    public UserUpdatePasswordPort userUpdatePasswordPort(
            UserRepositoryPort userRepository,
            PasswordEncoderPort passwordEncoder) {
        return new UpdatePasswordUseCase(userRepository, passwordEncoder);
    }

    /**
     * Bean do Use Case para busca de usuário por nome.
     * Implementa a lógica de consulta de usuário pelo username.
     *
     * @param userRepository porta para acesso ao repositório
     * @return implementação do UserFindByNamePort
     */
    @Bean
    public UserFindByNamePort userFindByNamePort(UserRepositoryPort userRepository) {
        return new FindUserByNameUseCase(userRepository);
    }

    /**
     * Bean do Use Case para busca de usuário por ID.
     * Implementa a lógica de consulta de usuário pelo identificador.
     *
     * @param userRepository porta para acesso ao repositório
     * @return implementação do UserFindByIdPort
     */
    @Bean
    public UserFindByIdPort userFindByIdPort(UserRepositoryPort userRepository) {
        return new FindUserByIdUseCase(userRepository);
    }
}