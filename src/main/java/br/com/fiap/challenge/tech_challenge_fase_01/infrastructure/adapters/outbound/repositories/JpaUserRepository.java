package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaUserEntity;

@Repository
public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    @Query("SELECT u FROM JpaUserEntity u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%',:name,'%')) AND u.isActive = true")
    public List<JpaUserEntity> findByName(@Param("name") String name);

    Optional<JpaUserEntity> findByLogin(String login);
    /**
     * Busca usuário por login de forma case-insensitive.
     * @param login Username do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<JpaUserEntity> findByLoginIgnoreCase(String login);
    /**
     * Busca usuário por email de forma case-insensitive.
     * @param email Email do usuário
     * @return Optional contendo o usuário se encontrado
     */
    Optional<JpaUserEntity> findByEmailIgnoreCase(String email);

}
