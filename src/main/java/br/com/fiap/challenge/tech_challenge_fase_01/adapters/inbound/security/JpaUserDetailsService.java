package br.com.fiap.challenge.tech_challenge_fase_01.adapters.inbound.security;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.User;
import br.com.fiap.challenge.tech_challenge_fase_01.ports.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JpaUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userDomain = userRepository.findByUsername(username);

        if (userDomain == null) {
            throw new UsernameNotFoundException("Usuário não encontrado: " + username);
        }

        List<SimpleGrantedAuthority> authorities = userDomain.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .toList();

        return new org.springframework.security.core.userdetails.User(
                userDomain.getLogin(),
                userDomain.getPassword(),
                userDomain.isActive(),
                true,
                true,
                true,
                authorities
        );
    }
}
