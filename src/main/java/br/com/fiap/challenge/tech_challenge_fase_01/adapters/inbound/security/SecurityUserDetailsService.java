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
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

    private final UserUseCases userUseCases;

    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        if (identifier == null || identifier.isBlank()) {
            throw new UsernameNotFoundException("Credenciais inválidas: identificador vazio.");
        }

        final String input = identifier.trim();
        final User user = userUseCases.findByUsername(input);

//        if (user == null) {
//            // e-mails costumam ser case-insensitive
//            user = userUseCases.findByUsername(input.toLowerCase(Locale.ROOT));
//        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + input);
        }

        final Collection<? extends GrantedAuthority> authorities =
                user.getRole() == null ? List.of() :
                        user.getRole().stream()
                                .filter(Objects::nonNull)
                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                                .toList();

        final String principal =
                Optional.ofNullable(user.getLogin())
                        .filter(s -> !s.isBlank())
                        .orElseGet(() -> Optional.ofNullable(user.getEmail()).orElse(input));

        return org.springframework.security.core.userdetails.User
                .withUsername(principal)
                .password(user.getPassword())   // precisa estar codificada pelo mesmo PasswordEncoder configurado
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(false)
                .credentialsExpired(false)
                .disabled(!user.isActive())
                .build();
    }
}
