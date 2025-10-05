package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.inbound.security;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;
import br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository.UserRepositoryPort;
import lombok.RequiredArgsConstructor;

/**
 * Implementação do UserDetailsService do Spring Security.
 * 
 * Responsabilidade (SOLID - SRP):
 * - Carregar dados de usuário para autenticação do Spring Security
 * - Converter UserDomain para UserDetails do Spring Security
 * 
 * Arquitetura Hexagonal:
 * - Adapter Inbound (driving adapter) para Spring Security
 * - Usa UserRepositoryPort (port outbound) para buscar usuários
 * - Mantém independência entre domínio e Spring Security
 */
@Service
@RequiredArgsConstructor
public class SecurityUserDetailsService implements UserDetailsService {

        private final UserRepositoryPort userRepository;

        @Override
        public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
                if (identifier == null || identifier.isBlank()) {
                        throw new UsernameNotFoundException("Credenciais inválidas: identificador vazio.");
                }

                final String input = identifier.trim();
                final UserDomain user = userRepository.findByUsername(input)
                                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + input));

                // Converte roles do domínio para authorities do Spring Security
                final Collection<? extends GrantedAuthority> authorities = user.getRole() == null ? List.of()
                                : user.getRole().stream()
                                                .filter(Objects::nonNull)
                                                .map(r -> new SimpleGrantedAuthority("ROLE_" + r.name()))
                                                .toList();

                // Usa login como principal, fallback para email
                final String principal = Optional.ofNullable(user.getLogin())
                                .filter(s -> !s.isBlank())
                                .orElseGet(() -> Optional.ofNullable(user.getEmail()).orElse(input));

                return org.springframework.security.core.userdetails.User
                                .withUsername(principal)
                                .password(user.getPassword()) // Senha já deve estar codificada
                                .authorities(authorities)
                                .accountExpired(false)
                                .accountLocked(false)
                                .credentialsExpired(false)
                                .disabled(!user.isActive())
                                .build();
        }
}
