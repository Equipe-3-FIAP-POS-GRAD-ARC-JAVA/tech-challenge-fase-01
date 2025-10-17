package br.com.fiap.challenge.tech_challenge_fase_01.application.usecase.user;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.service.UserDomainService;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.valueobject.Email;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.requests.UserCreateRequest;
import br.com.fiap.challenge.tech_challenge_fase_01.application.dto.response.UserResponse;
import br.com.fiap.challenge.tech_challenge_fase_01.application.mapper.UserMapper;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.inbound.user.UserCreatePort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.security.PasswordEncoderPort;

public class CreateUserUseCase implements UserCreatePort {

    private final UserRepositoryPort userRepository;
    private final UserDomainService userDomainService;
    private final PasswordEncoderPort passwordEncoder;

    public CreateUserUseCase(
            UserRepositoryPort userRepository,
            UserDomainService userDomainService,
            PasswordEncoderPort passwordEncoder) {
        this.userRepository = userRepository;
        this.userDomainService = userDomainService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponse create(UserCreateRequest userCreateRequest) {

        userDomainService.ensureUsernameIsUnique(userCreateRequest.login());
        userDomainService.ensureEmailIsUnique(Email.of(userCreateRequest.email()));

        String encryptedPassword = passwordEncoder.encode(userCreateRequest.password());

        var user = UserDomain.createClient(
                userCreateRequest.name(),
                userCreateRequest.email(),
                userCreateRequest.login(),
                encryptedPassword);

        var savedUser = userRepository.save(user);

        return UserMapper.toResponse(savedUser);
    }
}