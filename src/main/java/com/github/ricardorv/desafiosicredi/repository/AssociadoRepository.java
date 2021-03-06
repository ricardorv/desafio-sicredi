package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Associado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Repository
public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    Optional<Associado> findByUsuario(String usuario) throws EntityNotFoundException;

}
