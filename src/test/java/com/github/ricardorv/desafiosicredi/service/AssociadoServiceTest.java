package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.client.UserInfoClient;
import com.github.ricardorv.desafiosicredi.client.UserInfoDto;
import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import javax.persistence.EntityNotFoundException;

import static org.mockito.Mockito.when;

public class AssociadoServiceTest {

    AssociadoService associadoService;

    @Mock
    UserInfoClient userInfoClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        associadoService = new AssociadoServiceImpl(userInfoClient);
    }

    @Test
    void podeVotarTestApiUnableToVote() {
        when(userInfoClient.getUser("12345678901")).thenReturn(UserInfoDto
                .builder()
                .status(StatusCpfEnum.UNABLE_TO_VOTE)
                .build()
        );

        Assertions.assertDoesNotThrow(() -> {
            Boolean podeVotar = associadoService.podeVotar("12345678901");
            Assertions.assertEquals(Boolean.FALSE, podeVotar);
        });
    }

    @Test
    void podeVotarTestApiAbleToVote() {
        when(userInfoClient.getUser("12345678902")).thenReturn(UserInfoDto
                .builder()
                .status(StatusCpfEnum.ABLE_TO_VOTE)
                .build()
        );

        Assertions.assertDoesNotThrow(() -> {
            Boolean podeVotar = associadoService.podeVotar("12345678902");
            Assertions.assertEquals(Boolean.TRUE, podeVotar);
        });
    }

    @Test
    void podeVotarTestApiNotFound() {
        when(userInfoClient.getUser("12345678903")).thenThrow(EntityNotFoundException.class);

        Assertions.assertThrows(CpfInvalidoException.class, () -> {
            Boolean podeVotar = associadoService.podeVotar("12345678903");
        });
    }

}
