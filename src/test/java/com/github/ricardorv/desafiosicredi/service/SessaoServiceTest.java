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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jms.core.JmsTemplate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class SessaoServiceTest {

    private SessaoService sessaoService;

    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private AssociadoRepository associadoRepository;
    @Mock
    private VotoRepository votoRepository;
    @Mock
    private AssociadoService associadoService;
    @Mock
    private JmsTemplate jmsTemplate;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        sessaoService = new SessaoServiceImpl(
                pautaRepository,
                sessaoRepository,
                associadoRepository,
                votoRepository,
                associadoService,
                jmsTemplate
        );

    }

    @Test
    void iniciarSessaoMinDefaultTest() {
        when(pautaRepository.getOne(1L)).thenReturn(new Pauta(1L, "Pauta", new ArrayList<>()));
        when(sessaoRepository.save(any())).thenAnswer(invocationOnMock -> {
            Sessao sessao = invocationOnMock.getArgument(0);
            sessao.setId(1L);
            return sessao;
        });

        SessaoDto sessaoDto = sessaoService.iniciarSessao(
                PautaDto.builder().id(1L).build(),
                null);

        Assertions.assertEquals(1, sessaoDto.getId());
        Assertions.assertEquals(SessaoServiceImpl.DURACAO_SESSAO_EM_MINUTOS, sessaoDto.getDuracaoMinutos());
    }

    @Test
    void iniciarSessao20MinTest() {

        when(pautaRepository.getOne(1L)).thenReturn(new Pauta(1L, "Pauta", new ArrayList<>()));
        when(sessaoRepository.save(any())).thenAnswer(invocationOnMock -> {
            Sessao sessao = invocationOnMock.getArgument(0);
            sessao.setId(1L);
            return sessao;
        });

        SessaoDto sessaoDto = sessaoService.iniciarSessao(
                PautaDto.builder().id(1L).build(),
                20);

        Assertions.assertEquals(1, sessaoDto.getId());
        Assertions.assertEquals(20, sessaoDto.getDuracaoMinutos());
    }

    @Test
    void iniciarSessaoJaIniciada() {

        when(pautaRepository.getOne(1L)).thenReturn(new Pauta(1L, "Pauta", Arrays.asList(new Sessao())));

        Assertions.assertThrows(SessaoJaIniciadaException.class, () -> {
            sessaoService.iniciarSessao(
                    PautaDto.builder().id(1L).build(),
                    null
            );
        });
    }

    @Test
    void votar() {

        when(votoRepository.findBySessaoIdAndAssociadoUsuario(1L, "usuario"))
                .thenReturn(Optional.ofNullable(null));

        LocalDateTime now = LocalDateTime.now();

        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setDuracaoMinutos(10);
        sessao.setInicioSessao(now);
        sessao.setFimSessao(now.plusMinutes(10));
        when(sessaoRepository.getOne(1L)).thenReturn(sessao);

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setCpf("12312312300");
        associado.setUsuario("usuario");
        when(associadoRepository.findByUsuario("usuario")).thenReturn(Optional.ofNullable(associado));
        when(associadoService.podeVotar("12312312300")).thenReturn(Boolean.TRUE);

        sessaoService.votar(VotoDto.builder()
                .voto(VotoEnum.SIM)
                .cpf("12312312300")
                .usuario("usuario")
                .idSessao(1L)
                .build());

    }

    @Test
    void votarCpfNaoPode() {

        when(votoRepository.findBySessaoIdAndAssociadoUsuario(1L, "usuario"))
                .thenReturn(Optional.ofNullable(null));

        LocalDateTime now = LocalDateTime.now();

        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setDuracaoMinutos(10);
        sessao.setInicioSessao(now);
        sessao.setFimSessao(now.plusMinutes(10));
        when(sessaoRepository.getOne(1L)).thenReturn(sessao);

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setCpf("12312312300");
        associado.setUsuario("usuario");
        when(associadoRepository.findByUsuario("usuario")).thenReturn(Optional.ofNullable(associado));

        when(associadoService.podeVotar("12312312311")).thenReturn(Boolean.FALSE);

        Assertions.assertThrows(CpfNaoPodeVotarException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("12312312311")
                    .usuario("usuario")
                    .idSessao(1L)
                    .build());
        });
    }

    @Test
    void votarCpfInvalido() {

        when(votoRepository.findBySessaoIdAndAssociadoUsuario(1L, "usuario"))
                .thenReturn(Optional.ofNullable(null));

        LocalDateTime now = LocalDateTime.now();

        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setDuracaoMinutos(10);
        sessao.setInicioSessao(now);
        sessao.setFimSessao(now.plusMinutes(10));
        when(sessaoRepository.getOne(1L)).thenReturn(sessao);

        Associado associado = new Associado();
        associado.setId(1L);
        associado.setCpf("12312312300");
        associado.setUsuario("usuario");
        when(associadoRepository.findByUsuario("usuario")).thenReturn(Optional.ofNullable(associado));

        when(associadoService.podeVotar("12312312322")).thenThrow(CpfInvalidoException.class);

        Assertions.assertThrows(CpfInvalidoException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("12312312322")
                    .usuario("usuario")
                    .idSessao(1L)
                    .build());
        });
    }

    @Test
    void votarJaComputado() {

        when(votoRepository.findBySessaoIdAndAssociadoUsuario(1L, "usuario"))
                .thenReturn(Optional.ofNullable(new Voto()));

        Assertions.assertThrows(VotoJaComputadoException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("26161595036")
                    .usuario("usuario")
                    .idSessao(1L)
                    .build());
        });
    }

    @Test
    void votarSessaoJaExpirada() {

        LocalDateTime now = LocalDateTime.now();

        when(votoRepository.findBySessaoIdAndAssociadoUsuario(1L, "usuario"))
                .thenReturn(Optional.ofNullable(null));
        Sessao sessaoExpirada = new Sessao();
        sessaoExpirada.setId(3L);
        sessaoExpirada.setDuracaoMinutos(10);
        sessaoExpirada.setInicioSessao(now.minusMinutes(20));
        sessaoExpirada.setFimSessao(now.minusMinutes(10));
        when(sessaoRepository.getOne(3L)).thenReturn(sessaoExpirada);

        Assertions.assertThrows(SessaoJaExpirouException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("26161595036")
                    .usuario("usuario")
                    .idSessao(3L)
                    .build());
        });
    }

    @Test
    void contabilizarVotos() {

        LocalDateTime now = LocalDateTime.now();
        Sessao sessao = new Sessao();
        sessao.setId(1L);
        sessao.setDuracaoMinutos(10);
        sessao.setInicioSessao(now);
        sessao.setFimSessao(now.plusMinutes(10));
        when(sessaoRepository.getOne(1L)).thenReturn(sessao);

        List<Voto> votos = new ArrayList<>();
        votos.add(new Voto(1L, now, VotoEnum.NAO, sessao, new Associado()));
        votos.add(new Voto(2L, now, VotoEnum.NAO, sessao, new Associado()));
        votos.add(new Voto(3L, now, VotoEnum.SIM, sessao, new Associado()));
        votos.add(new Voto(4L, now, VotoEnum.SIM, sessao, new Associado()));
        votos.add(new Voto(5L, now, VotoEnum.SIM, sessao, new Associado()));
        doReturn(votos).when(votoRepository).findBySessaoId(1L);

        ResultadoVotacaoDto resultadoVotacaoDto = sessaoService.contabilizarVotos(1L);
        Assertions.assertNotNull(resultadoVotacaoDto.getQuantidadeVotos());
        Assertions.assertEquals(2L, resultadoVotacaoDto.getQuantidadeVotos().get(VotoEnum.NAO));
        Assertions.assertEquals(3L, resultadoVotacaoDto.getQuantidadeVotos().get(VotoEnum.SIM));
        Assertions.assertEquals(now.plusMinutes(10), resultadoVotacaoDto.getEncerramento());
    }

}
