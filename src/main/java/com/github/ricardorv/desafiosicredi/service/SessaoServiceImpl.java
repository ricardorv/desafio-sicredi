package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.AssociadoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.ResultadoVotacaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.SessaoDto;
import com.github.ricardorv.desafiosicredi.entity.Associado;
import com.github.ricardorv.desafiosicredi.entity.Pauta;
import com.github.ricardorv.desafiosicredi.entity.Sessao;
import com.github.ricardorv.desafiosicredi.entity.Voto;
import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaExpirouException;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaIniciadaException;
import com.github.ricardorv.desafiosicredi.exception.VotoJaComputadoException;
import com.github.ricardorv.desafiosicredi.repository.AssociadoRepository;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;
import com.github.ricardorv.desafiosicredi.repository.SessaoRepository;
import com.github.ricardorv.desafiosicredi.repository.VotoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SessaoServiceImpl implements SessaoService {

    PautaRepository pautaRepository;
    SessaoRepository sessaoRepository;
    AssociadoRepository associadoRepository;
    VotoRepository votoRepository;

    public SessaoServiceImpl(PautaRepository pautaRepository,
                             SessaoRepository sessaoRepository,
                             AssociadoRepository associadoRepository,
                             VotoRepository votoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
        this.associadoRepository = associadoRepository;
        this.votoRepository = votoRepository;
    }

    @Override
    public SessaoDto iniciarSessao(PautaDto pautaDto, Integer duracaoMinutos)
            throws SessaoJaIniciadaException {

        Pauta pauta = pautaRepository.getOne(pautaDto.getId());
        if (pauta.getSessoes() != null && !pauta.getSessoes().isEmpty()) {
            throw new SessaoJaIniciadaException();
        }

        Integer minutos = duracaoMinutos;
        if (minutos == null) {
            minutos = 10;
        }

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDuracaoMinutos(duracaoMinutos);
        sessao.setInicioSessao(LocalDateTime.now());
        sessao.setFimSessao(sessao.getInicioSessao().plusMinutes(minutos));

        sessao = sessaoRepository.save(sessao);

        return SessaoDto.builder()
                .id(sessao.getId())
                .duracaoMinutos(sessao.getDuracaoMinutos())
                .inicioSessao(sessao.getInicioSessao())
                .fimSessao(sessao.getFimSessao())
                .build();

    }

    @Override
    public void votar(SessaoDto sessaoDto, AssociadoDto associadoDto, VotoEnum votoEnum)
            throws VotoJaComputadoException, SessaoJaExpirouException, EntityNotFoundException {

        LocalDateTime localDateTimeAtual = LocalDateTime.now();

        Optional<Voto> votoOpt = votoRepository
                .findBySessaoIdAndAssociadoId(sessaoDto.getId(), associadoDto.getId());

        if (votoOpt.isPresent()) {
            throw new VotoJaComputadoException();
        }


        Sessao sessao = sessaoRepository.getOne(sessaoDto.getId());
        if (localDateTimeAtual.isAfter(sessao.getFimSessao())) {
            throw new SessaoJaExpirouException();
        }

        Associado associado = associadoRepository.findByToken(associadoDto.getToken());

        Voto voto = new Voto();
        voto.setAssociado(associado);
        voto.setDthrInserido(localDateTimeAtual);
        voto.setSessao(sessao);
        voto.setVoto(votoEnum);
        votoRepository.save(voto);

    }

    @Override
    public ResultadoVotacaoDto contabilizarVotos(SessaoDto sessao) throws EntityNotFoundException {
        return null;
    }

}
