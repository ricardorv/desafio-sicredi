package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.AssociadoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.ResultadoVotacaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.SessaoDto;
import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaExpirouException;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaIniciadaException;
import com.github.ricardorv.desafiosicredi.exception.VotoJaComputadoException;

import javax.persistence.EntityNotFoundException;

public interface SessaoService {

    SessaoDto iniciarSessao (SessaoDto sessao, PautaDto pauta, Integer duracaoMinutos)
            throws SessaoJaIniciadaException;

    void votar(SessaoDto sessao, AssociadoDto associado, VotoEnum voto)
            throws VotoJaComputadoException, SessaoJaExpirouException, EntityNotFoundException;

    ResultadoVotacaoDto contabilizarVotos(SessaoDto sessao)
            throws EntityNotFoundException;

}
