package com.github.ricardorv.desafiosicredi.repository;

import com.github.ricardorv.desafiosicredi.entity.Sessao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoRepository extends JpaRepository<Sessao, Long> {
}
