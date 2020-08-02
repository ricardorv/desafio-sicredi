package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.entity.Pauta;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

public class PautaServiceImpl implements PautaService {

    PautaRepository pautaRepository;

    public PautaServiceImpl(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    @Override
    public PautaDto cadastrarPauta(String nome) {
        Pauta pauta = new Pauta();
        pauta.setNome(nome);
        pauta = pautaRepository.save(pauta);

        return PautaDto.builder()
                .id(pauta.getId())
                .nome(pauta.getNome())
                .build();
    }

    @Override
    public List<PautaDto> buscarPautas() {
        List<Pauta> pautas = pautaRepository.findAll();

        return pautas.stream().map(pauta -> PautaDto.builder()
                .id(pauta.getId())
                .nome(pauta.getNome())
                .build())
                .collect(Collectors.toList());
    }

    @Override
    public PautaDto buscarPauta(Long id) throws EntityNotFoundException {
        Pauta pauta = pautaRepository.getOne(id);
        return PautaDto.builder()
                .id(pauta.getId())
                .nome(pauta.getNome())
                .build();
    }

}
