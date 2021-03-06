package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VotoRepository extends JpaRepository<Voto, Long> {

    Optional<Voto> findBySessaoIdAndAssociadoUsuario(Long sessaoId, String associadoUsuario);

    List<Voto> findBySessaoId(Long sessaoId);

}
