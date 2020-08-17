package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.ResultadoVotacaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.SessaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.VotoDto;
import com.github.ricardorv.desafiosicredi.entity.Associado;
import com.github.ricardorv.desafiosicredi.entity.Pauta;
import com.github.ricardorv.desafiosicredi.entity.Sessao;
import com.github.ricardorv.desafiosicredi.entity.Voto;
import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import com.github.ricardorv.desafiosicredi.exception.*;
import com.github.ricardorv.desafiosicredi.repository.AssociadoRepository;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;
import com.github.ricardorv.desafiosicredi.repository.SessaoRepository;
import com.github.ricardorv.desafiosicredi.repository.VotoRepository;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SessaoServiceImpl implements SessaoService {

    public static Integer DURACAO_SESSAO_EM_MINUTOS = 1;

    private final PautaRepository pautaRepository;
    private final SessaoRepository sessaoRepository;
    private final AssociadoRepository associadoRepository;
    private final VotoRepository votoRepository;
    private final AssociadoService associadoService;

    public SessaoServiceImpl(PautaRepository pautaRepository,
                             SessaoRepository sessaoRepository,
                             AssociadoRepository associadoRepository,
                             VotoRepository votoRepository,
                             AssociadoService associadoService) {
        this.pautaRepository = pautaRepository;
        this.sessaoRepository = sessaoRepository;
        this.associadoRepository = associadoRepository;
        this.votoRepository = votoRepository;
        this.associadoService = associadoService;
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
            minutos = DURACAO_SESSAO_EM_MINUTOS;
        }

        Sessao sessao = new Sessao();
        sessao.setPauta(pauta);
        sessao.setDuracaoMinutos(minutos);
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
    public void votar(VotoDto votoDto)
            throws VotoJaComputadoException, SessaoJaExpirouException, EntityNotFoundException {

        LocalDateTime localDateTimeAtual = LocalDateTime.now();

        Optional<Voto> votoOpt = votoRepository
                .findBySessaoIdAndAssociadoToken(votoDto.getIdSessao(), votoDto.getToken());

        if (votoOpt.isPresent()) {
            throw new VotoJaComputadoException();
        }


        Sessao sessao = sessaoRepository.getOne(votoDto.getIdSessao());
        if (localDateTimeAtual.isAfter(sessao.getFimSessao())) {
            throw new SessaoJaExpirouException();
        }

        Optional<Associado> associadoOpt = associadoRepository.findByToken(votoDto.getToken());
        if (!associadoOpt.isPresent()) {
            throw new EntityNotFoundException();
        }

        Boolean podeVotar = associadoService.podeVotar(votoDto.getCpf());
        if(!podeVotar) {
            throw new CpfNaoPodeVotarException();
        }

        Associado associado = associadoOpt.get();
        associado.setCpf(votoDto.getCpf());
        associadoRepository.save(associado);

        Voto voto = new Voto();
        voto.setAssociado(associado);
        voto.setDthrInserido(localDateTimeAtual);
        voto.setSessao(sessao);
        voto.setVoto(votoDto.getVoto());
        votoRepository.save(voto);

    }

    @Override
    public ResultadoVotacaoDto contabilizarVotos(SessaoDto sessaoDto) throws EntityNotFoundException {

        Sessao sessao = sessaoRepository.getOne(sessaoDto.getId());
        List<Voto> votos = votoRepository.findBySessaoId(sessaoDto.getId());

        Map<VotoEnum, Long> votosMap = votos.stream()
                .collect(Collectors.groupingBy(o -> o.getVoto(), Collectors.counting()));

        return ResultadoVotacaoDto.builder()
                .quantidadeVotos(votosMap)
                .encerramento(sessao.getFimSessao())
                .build();

    }

}
