package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.response.enumx.RolesEnum;
import br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security.dto.AuthenticatedUserDto;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private static final String USERNAME_OR_PASSWORD_INVALID = "Invalid username or password.";

    private final UserUseCases userUseCases;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        final AuthenticatedUserDto authenticatedUser = userUseCases.findAuthenticatedUserByUsername(username);

        if (Objects.isNull(authenticatedUser)) {
            throw new UsernameNotFoundException(USERNAME_OR_PASSWORD_INVALID);
        }

        final String authenticatedUsername = authenticatedUser.getUsername();
        final String authenticatedPassword = authenticatedUser.getPassword();
        final List<RolesEnum> userRoles = authenticatedUser.getUserRole();

        final List<GrantedAuthority> authorities =
                userRoles == null ? List.of()
                        : userRoles.stream()
                        .filter(Objects::nonNull)
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role.name()))
                        .collect(Collectors.toList());

        return new User(authenticatedUsername, authenticatedPassword, authorities);
    }
}
