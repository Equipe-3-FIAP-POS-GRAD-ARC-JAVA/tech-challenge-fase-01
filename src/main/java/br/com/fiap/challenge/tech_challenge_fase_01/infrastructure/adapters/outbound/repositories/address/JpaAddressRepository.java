package br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.repositories.address;

import br.com.fiap.challenge.tech_challenge_fase_01.infrastructure.adapters.outbound.entities.JpaAddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface JpaAddressRepository extends JpaRepository<JpaAddressEntity, UUID> {

    Optional<JpaAddressEntity> findTopByUserIdOrderByCreatedAtDesc(UUID userId);

    List<JpaAddressEntity> findAllByUserIdOrderByCreatedAtDesc(UUID userId);
}
