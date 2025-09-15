package br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.fiap.challenge.tech_challenge_fase_01.adapters.outbound.entities.JpaUserEntity;

public interface JpaUserRepository extends JpaRepository<JpaUserEntity, String> {

    @Query("SELECT u FROM JpaUserEntity u WHERE u.name LIKE CONCAT('%',:name,'%') AND u.isActive = true")
    public List<JpaUserEntity> findByName(@Param("name") String name);

}
