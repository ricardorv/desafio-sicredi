package com.github.ricardorv.desafiosicredi.api.v1.controller;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.ResultadoVotacaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.SessaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.VotoDto;
import com.github.ricardorv.desafiosicredi.service.PautaService;
import com.github.ricardorv.desafiosicredi.service.SessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PautaController {

    private final PautaService pautaService;
    private final SessaoService sessaoService;

    public PautaController(PautaService pautaService, SessaoService sessaoService) {
        this.pautaService = pautaService;
        this.sessaoService = sessaoService;
    }

    @GetMapping("/v1/pauta")
    @ResponseStatus(HttpStatus.OK)
    public List<PautaDto> all() {
        return pautaService.buscarPautas();
    }

    @GetMapping("/v1/pauta/{id}")
    @ResponseStatus(HttpStatus.OK)
    public PautaDto one(@PathVariable Long id) {
        return pautaService.buscarPauta(id);
    }

    @PostMapping("/v1/pauta")
    @ResponseStatus(HttpStatus.CREATED)
    public PautaDto newPauta(@RequestBody PautaDto pauta) {
        return pautaService.cadastrarPauta(pauta.getNome());
    }

    @PostMapping("/v1/pauta/{idPauta}/sessao")
    @ResponseStatus(HttpStatus.CREATED)
    public SessaoDto newSessao(
            @PathVariable Long idPauta,
            @RequestParam(required = false) Integer duracaoMinutos) {
        return sessaoService.iniciarSessao(PautaDto.builder().id(idPauta).build(), duracaoMinutos);
    }

    @PostMapping("/v1/pauta/{idPauta}/sessao/{idSessao}/voto")
    @ResponseStatus(HttpStatus.CREATED)
    public void votar(
            @PathVariable Long idPauta,
            @PathVariable Long idSessao,
            @RequestBody VotoDto votoDto) {
        votoDto.setIdSessao(idSessao);
        sessaoService.votar(votoDto);
    }

    @GetMapping("/v1/pauta/{idPauta}/sessao/{idSessao}/voto")
    @ResponseStatus(HttpStatus.OK)
    public ResultadoVotacaoDto contabilizar(
            @PathVariable Long idPauta,
            @PathVariable Long idSessao) {
        return sessaoService.contabilizarVotos(idSessao);
    }

}
