package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VotoRepository extends JpaRepository<Voto, Long> {

    Optional<Voto> findBySessaoIdAndAssociadoId(Long sessaoId, Long associadoId);

}
