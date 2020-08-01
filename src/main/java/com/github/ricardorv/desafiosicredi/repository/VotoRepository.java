package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Voto;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, Long> {
}
