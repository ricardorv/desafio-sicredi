package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.*;
import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaExpirouException;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaIniciadaException;
import com.github.ricardorv.desafiosicredi.exception.VotoJaComputadoException;

import javax.persistence.EntityNotFoundException;

public interface SessaoService {

    SessaoDto iniciarSessao (PautaDto pauta, Integer duracaoMinutos)
            throws SessaoJaIniciadaException;

    void votar(VotoDto votoDto)
            throws VotoJaComputadoException, SessaoJaExpirouException, EntityNotFoundException;

    ResultadoVotacaoDto contabilizarVotos(Long idSessao)
            throws EntityNotFoundException;

}
