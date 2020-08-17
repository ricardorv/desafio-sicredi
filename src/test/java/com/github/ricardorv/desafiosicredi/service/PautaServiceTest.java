package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.entity.Pauta;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class PautaServiceTest {

    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        pautaService = new PautaServiceImpl(pautaRepository);
    }

    @Test
    void cadastrarPautaTest() {
        when(pautaRepository.save(any())).thenReturn(new Pauta(1L, "Pauta", new ArrayList<>()));

        PautaDto pautaDto = pautaService.cadastrarPauta("Pauta");
        Assertions.assertNotNull(pautaDto);
        Assertions.assertEquals("Pauta", pautaDto.getNome());
        Assertions.assertEquals(1L, pautaDto.getId());
    }

    @Test
    void buscarPautasTest() {
        Pauta pauta = new Pauta(1L, "Pauta", new ArrayList<>());
        when(pautaRepository.findAll()).thenReturn(Arrays.asList(pauta));

        List<PautaDto> pautasDto = pautaService.buscarPautas();
        Assertions.assertNotNull(pautasDto);
        Assertions.assertEquals(1, pautasDto.size());
        Assertions.assertEquals("Pauta", pautasDto.get(0).getNome());
    }

    @Test
    void buscarPautaTest() {
        when(pautaRepository.getOne(1L)).thenReturn(new Pauta(1L, "Pauta", new ArrayList<>()));

        PautaDto pautaDto = pautaService.buscarPauta(1L);
        Assertions.assertNotNull(pautaDto);
        Assertions.assertEquals("Pauta", pautaDto.getNome());
        Assertions.assertEquals(1L, pautaDto.getId());
    }

    @Test
    void buscarPautaNaoExisteTest() {
        when(pautaRepository.getOne(2L)).thenThrow(EntityNotFoundException.class);
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            PautaDto pautaDto = pautaService.buscarPauta(2L);
        });
    }
}
