package br.com.fiap.challenge.tech_challenge_fase_01.application.ports.outbound.repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import br.com.fiap.challenge.tech_challenge_fase_01.application.domain.user.UserDomain;

/**
 * Port Outbound para operações de persistência de usuário.
 * 
 * Seguindo a Arquitetura Hexagonal, esta interface:
 * - Trabalha APENAS com objetos de domínio (UserDomain)
 * - NÃO conhece DTOs (que são artefatos da camada de infraestrutura)
 * - Define o contrato para adaptadores de persistência
 * 
 * A conversão Domain <-> DTO deve ser feita na camada de infraestrutura.
 */
public interface UserRepositoryPort {

    /**
     * Salva um novo usuário ou atualiza um existente.
     * 
     * @param user Objeto de domínio do usuário
     * @return Usuário persistido
     */
    UserDomain save(UserDomain user);

    /**
     * Busca um usuário por ID.
     * 
     * @param id ID do usuário (UUID)
     * @return Optional contendo o usuário se encontrado
     */
    Optional<UserDomain> findById(UUID id);

    /**
     * Busca usuários por nome (pode retornar vários resultados).
     * 
     * @param name Nome ou parte do nome do usuário
     * @return Lista de usuários encontrados
     */
    List<UserDomain> findByName(String name);

    /**
     * Busca um usuário por login.
     * 
     * @param login Login do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<UserDomain> findByLogin(String login);

    /**
     * Remove um usuário.
     * 
     * @param id ID do usuário a ser removido (UUID)
     */
    void delete(UUID id);

    /**
     * Verifica se existe um usuário com o username informado.
     * 
     * @param username Username a verificar
     * @return true se existe, false caso contrário
     */
    boolean existsByUsername(String username);

    /**
     * Busca um usuário por email.
     * 
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<UserDomain> findByEmail(String email);

    

}
