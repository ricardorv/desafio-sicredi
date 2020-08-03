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
import com.github.ricardorv.desafiosicredi.exception.SessaoJaExpirouException;
import com.github.ricardorv.desafiosicredi.exception.SessaoJaIniciadaException;
import com.github.ricardorv.desafiosicredi.exception.VotoJaComputadoException;
import com.github.ricardorv.desafiosicredi.repository.AssociadoRepository;
import com.github.ricardorv.desafiosicredi.repository.PautaRepository;
import com.github.ricardorv.desafiosicredi.repository.SessaoRepository;
import com.github.ricardorv.desafiosicredi.repository.VotoRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SessaoServiceTest {

    @Mock
    private SessaoService sessaoService;

    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private SessaoRepository sessaoRepository;
    @Mock
    private AssociadoRepository associadoRepository;
    @Mock
    private VotoRepository votoRepository;

    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        now = LocalDateTime.now();

        // pautaRepository
        Pauta pauta = new Pauta(Long.valueOf(1), "Pauta", new ArrayList<>());
        Pauta pautaComSessao = new Pauta(Long.valueOf(2), "Pauta com sessao",
                Arrays.asList(Sessao.builder().id(Long.valueOf(1)).build()));
        doReturn(pauta).when(pautaRepository).getOne(Long.valueOf(1));
        doReturn(pautaComSessao).when(pautaRepository).getOne(Long.valueOf(2));


        // sessaoRepository
        when(sessaoRepository.save(any())).thenAnswer(invocationOnMock -> {
            Sessao sessao = invocationOnMock.getArgument(0);
            sessao.setId(Long.valueOf(1));
            return sessao;
        });
        Sessao sessao = new Sessao();
        sessao.setId(Long.valueOf(1));
        sessao.setDuracaoMinutos(10);
        sessao.setInicioSessao(now);
        sessao.setFimSessao(now.plusMinutes(10));
        doReturn(sessao).when(sessaoRepository).getOne(Long.valueOf(1));

        Sessao sessaoExpirada = new Sessao();
        sessaoExpirada.setId(Long.valueOf(3));
        sessaoExpirada.setDuracaoMinutos(10);
        sessaoExpirada.setInicioSessao(now.minusMinutes(20));
        sessaoExpirada.setFimSessao(now.minusMinutes(10));
        doReturn(sessaoExpirada).when(sessaoRepository).getOne(Long.valueOf(3));


        // associadoRepository
        Associado associado = new Associado();
        associado.setId(Long.valueOf(1));
        associado.setCpf("26161595036");
        associado.setToken("token");
        doReturn(Optional.ofNullable(associado)).when(associadoRepository).findByToken("token");


        // votoRepository
        doReturn(Optional.ofNullable(null)).when(votoRepository).findBySessaoIdAndAssociadoToken(
                Long.valueOf(1),
                "token"
        );
        doReturn(Optional.ofNullable(new Voto())).when(votoRepository).findBySessaoIdAndAssociadoToken(
                Long.valueOf(2),
                "token"
        );
        doReturn(Optional.ofNullable(null)).when(votoRepository).findBySessaoIdAndAssociadoToken(
                Long.valueOf(3),
                "token"
        );
        when(votoRepository.save(any())).thenAnswer(invocationOnMock -> {
            Voto voto = invocationOnMock.getArgument(0);
            voto.setId(Long.valueOf(1));
            return voto;
        });
        List<Voto> votos = new ArrayList<>();
        votos.add(new Voto(Long.valueOf(1), now, VotoEnum.NAO, sessao, associado));
        votos.add(new Voto(Long.valueOf(2), now, VotoEnum.NAO, sessao, associado));
        votos.add(new Voto(Long.valueOf(3), now, VotoEnum.SIM, sessao, associado));
        votos.add(new Voto(Long.valueOf(4), now, VotoEnum.SIM, sessao, associado));
        votos.add(new Voto(Long.valueOf(5), now, VotoEnum.SIM, sessao, associado));
        doReturn(votos).when(votoRepository).findBySessaoId(Long.valueOf(1));


        sessaoService = new SessaoServiceImpl(
                pautaRepository,
                sessaoRepository,
                associadoRepository,
                votoRepository
        );

    }

    @Test
    void iniciarSessaoMinDefaultTest() {
        SessaoDto sessaoDto = sessaoService.iniciarSessao(
                PautaDto.builder().id(Long.valueOf(1)).build(),
                null);

        Assertions.assertEquals(1, sessaoDto.getId());
        Assertions.assertEquals(SessaoServiceImpl.DURACAO_SESSAO_EM_MINUTOS, sessaoDto.getDuracaoMinutos());

    }

    @Test
    void iniciarSessao20MinTest() {
        SessaoDto sessaoDto = sessaoService.iniciarSessao(
                PautaDto.builder().id(Long.valueOf(1)).build(),
                20);

        Assertions.assertEquals(1, sessaoDto.getId());
        Assertions.assertEquals(20, sessaoDto.getDuracaoMinutos());
    }

    @Test
    void iniciarSessaoPeriodoSessao() {
        //TODO Passar o clock para o service para poder testar as datas
    }

    @Test
    void iniciarSessaoJaIniciada() {
        Assertions.assertThrows(SessaoJaIniciadaException.class, () -> {
            sessaoService.iniciarSessao(
                    PautaDto.builder().id(Long.valueOf(2)).build(),
                    null
            );
        });
    }

    @Test
    void votar() {
        sessaoService.votar(VotoDto.builder()
                .voto(VotoEnum.SIM)
                .cpf("26161595036")
                .token("token")
                .idSessao(Long.valueOf(1))
                .build());

    }

    @Test
    void votarJaComputado() {
        Assertions.assertThrows(VotoJaComputadoException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("26161595036")
                    .token("token")
                    .idSessao(Long.valueOf(2))
                    .build());
        });
    }

    @Test
    void votarSessaoJaExpirada() {
        Assertions.assertThrows(SessaoJaExpirouException.class, () -> {
            sessaoService.votar(VotoDto.builder()
                    .voto(VotoEnum.SIM)
                    .cpf("26161595036")
                    .token("token")
                    .idSessao(Long.valueOf(3))
                    .build());
        });
    }

    @Test
    void contabilizarVotos() {
        ResultadoVotacaoDto resultadoVotacaoDto = sessaoService.contabilizarVotos(SessaoDto.builder().id(Long.valueOf(1)).build());
        Assertions.assertNotNull(resultadoVotacaoDto.getQuantidadeVotos());
        Assertions.assertEquals(Long.valueOf(2), resultadoVotacaoDto.getQuantidadeVotos().get(VotoEnum.NAO));
        Assertions.assertEquals(Long.valueOf(3), resultadoVotacaoDto.getQuantidadeVotos().get(VotoEnum.SIM));
        Assertions.assertEquals(now.plusMinutes(10), resultadoVotacaoDto.getEncerramento());
    }

}
