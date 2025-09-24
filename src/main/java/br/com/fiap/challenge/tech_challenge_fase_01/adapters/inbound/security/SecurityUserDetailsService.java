package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.usecases.UserUseCases;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserUseCases userUseCases;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        final User user = userUseCases.findByUsername(username);
        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found: " + username);
        }
        final Collection<? extends GrantedAuthority> authorities =
                user.getRole() == null ? List.of() :
                        user.getRole().stream()
                                .filter(Objects::nonNull)
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                                .toList();

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getLogin())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }
}
