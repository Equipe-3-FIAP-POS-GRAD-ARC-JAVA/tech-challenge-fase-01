package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.address;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;

@Repository
public interface JpaAddressRepository extends JpaRepository<JpaAddressEntity, UUID> {

    Optional<JpaAddressEntity> findTopByUser_IdOrderByCreatedAtDesc(UUID userId);

    List<JpaAddressEntity> findAllByUser_IdOrderByCreatedAtDesc(UUID userId);
}
