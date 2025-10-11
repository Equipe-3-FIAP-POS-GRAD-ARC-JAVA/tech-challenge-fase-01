package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.configs;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Getter;
import lombok.Setter;

/**
 * Configuração de beans de segurança do Spring Security.
 *
 * Responsabilidades (SOLID - SRP):
 * - Configurar componentes de autenticação e autorização
 * - Definir beans de segurança (PasswordEncoder, AuthenticationManager, AuthenticationProvider)
 * - Fornecer configurações de segurança flexíveis e externalizadas
 *
 * Arquitetura Hexagonal:
 * - Configuração da infraestrutura de segurança
 * - Mantém dependências de segurança isoladas
 * - Permite troca de implementações sem afetar o core
 * 
 * Princípios SOLID aplicados:
 * - OCP: Extensível através de configurações externas
 * - DIP: Depende de abstrações (PasswordEncoder, UserDetailsService)
 */
@Configuration
public class SecurityConfig {

    /**
     * Configurações de senha externalizadas.
     * Permite configurar diferentes algoritmos e parâmetros via properties.
     */
    @ConfigurationProperties(prefix = "security.password")
    @Getter
    @Setter
    public static class PasswordConfig {
        private String algorithm = "bcrypt";
        private int bcryptStrength = 12;
        private boolean hideUserNotFoundExceptions = false;
    }

    /**
     * Bean das configurações de senha.
     * Permite configuração externa via application.properties/yaml.
     */
    @Bean
    public PasswordConfig passwordConfig() {
        return new PasswordConfig();
    }

    /**
     * Bean do Spring Security PasswordEncoder configurável.
     * Usa BCrypt com fator de custo configurável para hash de senhas.
     * Aplica OCP - pode ser estendido para outros algoritmos sem modificação.
     *
     * @param config configurações de senha
     * @return implementação configurável do PasswordEncoder
     */
    @Bean
    public PasswordEncoder passwordEncoder(PasswordConfig config) {
        return createPasswordEncoder(config);
    }

    /**
     * Factory method para criar PasswordEncoder baseado na configuração.
     * Implementa Strategy pattern para diferentes algoritmos.
     * Preparado para extensão futura com outros algoritmos (Argon2, PBKDF2, etc).
     */
    private PasswordEncoder createPasswordEncoder(PasswordConfig config) {
        String algorithm = config.getAlgorithm().toLowerCase();
        
        if ("bcrypt".equals(algorithm)) {
            return new BCryptPasswordEncoder(config.getBcryptStrength());
        }
        
        // Padrão: BCrypt caso o algoritmo não seja reconhecido
        return new BCryptPasswordEncoder(config.getBcryptStrength());
    }

    /**
     * Bean do AuthenticationManager do Spring Security.
     * Gerencia o processo de autenticação no sistema.
     *
     * @param config configuração de autenticação do Spring
     * @return AuthenticationManager configurado
     * @throws Exception se houver erro na configuração
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean do AuthenticationProvider configurável.
     * Define como o Spring Security deve autenticar usuários via DAO.
     * Aplica DIP - depende de abstrações (UserDetailsService, PasswordEncoder).
     *
     * @param userDetailsService serviço para carregar dados do usuário
     * @param passwordEncoder encoder para validação de senhas
     * @param config configurações de segurança
     * @return AuthenticationProvider configurado com DAO
     */
    @Bean
    @SuppressWarnings("deprecation")
    public AuthenticationProvider authenticationProvider(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder,
            PasswordConfig config) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(config.isHideUserNotFoundExceptions());

        return provider;
    }
}