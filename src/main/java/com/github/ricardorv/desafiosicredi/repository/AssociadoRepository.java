package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Associado;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

public interface AssociadoRepository extends JpaRepository<Associado, Long> {

    Associado findByToken(String token) throws EntityNotFoundException;

}
