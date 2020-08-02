package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface PautaService {

    PautaDto cadastrarPauta(String nome);

    List<PautaDto> buscarPautas();

    PautaDto buscarPauta() throws EntityNotFoundException;

}
