package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.repositories;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, UUID> {

    @Query("SELECT u FROM JpaUserEntity u WHERE LOWER(u.name) LIKE LOWER(CONCAT('%',:name,'%')) AND u.isActive = true")
    public List<JpaUserEntity> findByName(@Param("name") String name);

    Optional<JpaUserEntity> findByEmail(String email);
    Optional<JpaUserEntity> findByLogin(String login);

}
