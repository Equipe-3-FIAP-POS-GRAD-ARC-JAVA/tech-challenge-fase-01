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
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.AddressRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateOwnerUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.CreateUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.DeleteUserUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByIdUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.FindUserByNameUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdatePasswordUseCase;
import br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user.UpdateUserUseCase;

@Configuration
public class UserUseCaseConfig {

    @Bean
    public UserDomainService userDomainService(UserRepositoryPort userRepository) {
        return new UserDomainService(userRepository);
    }

    @Bean
    public UserCreatePort userCreatePort(
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateUserUseCase(userRepository, addressRepository, userDomainService, passwordEncoder);
    }

    @Bean
    public UserCreateOwnerPort userCreateOwnerPort(
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        return new CreateOwnerUseCase(userRepository, addressRepository, userDomainService, passwordEncoder);
    }

    @Bean
    public UserDeletePort userDeletePort(UserRepositoryPort userRepository) {
        return new DeleteUserUseCase(userRepository);
    }

    @Bean
    public UserUpdatePort userUpdatePort(UserRepositoryPort userRepository, AddressRepositoryPort addressRepository) {
        return new UpdateUserUseCase(userRepository, addressRepository);
    }

    @Bean
    public UserUpdatePasswordPort userUpdatePasswordPort(
            UserRepositoryPort userRepository,
            AddressRepositoryPort addressRepository,
            PasswordEncoderPort passwordEncoder) {
        return new UpdatePasswordUseCase(userRepository, addressRepository, passwordEncoder);
    }

    @Bean
    public UserFindByNamePort userFindByNamePort(UserRepositoryPort userRepository, AddressRepositoryPort addressRepository) {
        return new FindUserByNameUseCase(userRepository, addressRepository);
    }

    @Bean
    public UserFindByIdPort userFindByIdPort(UserRepositoryPort userRepository, AddressRepositoryPort addressRepository) {
        return new FindUserByIdUseCase(userRepository, addressRepository);
    }
}
