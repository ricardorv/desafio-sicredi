package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AssociadoServiceTest {

    @Mock
    AssociadoService associadoService;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        associadoService = new AssociadoServiceImpl();
    }

    @Test
    void podeVotarTestApiUnableToVote() {
        Assertions.assertDoesNotThrow(() -> {
            Boolean podeVotar = associadoService.podeVotar("12485386099");
            //Assertions.assertEquals(Boolean.FALSE, podeVotar);
        });
    }

    @Test
    void podeVotarTestApiNotFound() {
        Assertions.assertThrows(CpfInvalidoException.class, () -> {
            associadoService.podeVotar("12312312300");
        });
    }

}
