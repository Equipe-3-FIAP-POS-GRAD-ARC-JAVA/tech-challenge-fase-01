package br.com.fiap.challenge.tech_challenge_fase_01.application.service;

import br.com.fiap.challenge.tech_challenge_fase_01.domain.user.UserResponseDTO;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AppUserDetailsService implements UserDetailsService {

    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    public AppUserDetailsService(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserResponseDTO> users = userService.findByName(username);
        var u = users.stream().findFirst()
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        String encodedPassword = u.password();
        var authorities = u.roles().stream()
                .map(r -> new SimpleGrantedAuthority("ROLE_" + r))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                u.name(),
                encodedPassword,
                authorities
        );
    }
}
