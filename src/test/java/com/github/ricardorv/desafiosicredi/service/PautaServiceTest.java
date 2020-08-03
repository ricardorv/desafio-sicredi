package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.entity.Pauta;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@SpringBootTest
public class PautaServiceTest {

    @Mock
    private PautaService pautaService;

    @Mock
    private PautaRepository pautaRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        Pauta pauta = new Pauta(Long.valueOf(1), "Pauta", new ArrayList<>());

        doReturn(pauta).when(pautaRepository).save(any());
        doReturn(Arrays.asList(pauta)).when(pautaRepository).findAll();
        doReturn(pauta).when(pautaRepository).getOne(Long.valueOf(1));
        doThrow(EntityNotFoundException.class).when(pautaRepository).getOne(Long.valueOf(2));

        pautaService = new PautaServiceImpl(pautaRepository);
    }

    @Test
    void cadastrarPautaTest() {
        PautaDto pautaDto = pautaService.cadastrarPauta("Pauta");
        Assertions.assertNotNull(pautaDto);
        Assertions.assertEquals("Pauta", pautaDto.getNome());
        Assertions.assertEquals(Long.valueOf(1), pautaDto.getId());
    }

    @Test
    void buscarPautasTest() {
        List<PautaDto> pautasDto = pautaService.buscarPautas();
        Assertions.assertNotNull(pautasDto);
        Assertions.assertEquals(1, pautasDto.size());
        Assertions.assertEquals("Pauta", pautasDto.get(0).getNome());
    }

    @Test
    void buscarPautaTest() {
        PautaDto pautaDto = pautaService.buscarPauta(Long.valueOf(1));
        Assertions.assertNotNull(pautaDto);
        Assertions.assertEquals("Pauta", pautaDto.getNome());
        Assertions.assertEquals(Long.valueOf(1), pautaDto.getId());
    }

    @Test
    void buscarPautaNaoExisteTest() {
        Assertions.assertThrows(EntityNotFoundException.class, () -> {
            PautaDto pautaDto = pautaService.buscarPauta(Long.valueOf(2));
        });
    }
}
