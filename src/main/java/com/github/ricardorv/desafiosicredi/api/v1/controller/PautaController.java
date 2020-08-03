package com.github.ricardorv.desafiosicredi.api.v1.controller;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.SessaoDto;
import com.github.ricardorv.desafiosicredi.api.v1.dto.VotoDto;
import com.github.ricardorv.desafiosicredi.service.PautaService;
import com.github.ricardorv.desafiosicredi.service.SessaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PautaController {

    private PautaService pautaService;
    private SessaoService sessaoService;

    public PautaController(PautaService pautaService, SessaoService sessaoService) {
        this.pautaService = pautaService;
        this.sessaoService = sessaoService;
    }

    @GetMapping("/v1/pauta")
    public ResponseEntity<List<PautaDto>> all() {
        return ResponseEntity.ok(pautaService.buscarPautas());
    }

    @GetMapping("/v1/pauta/{id}")
    public ResponseEntity<PautaDto> one(@PathVariable Long id) {
        return ResponseEntity.ok(pautaService.buscarPauta(id));
    }

    @PostMapping("/v1/pauta")
    public ResponseEntity<PautaDto> newPauta(@RequestBody PautaDto pauta) {

        return ResponseEntity.status(HttpStatus.CREATED).body(
                pautaService.cadastrarPauta(pauta.getNome()));

    }

    @PostMapping("/v1/pauta/{idPauta}/sessao")
    public ResponseEntity<SessaoDto> newSessao(
            @PathVariable Long idPauta,
            @RequestParam(required = false) Integer duracaoMinutos) {

        PautaDto pautaDto = PautaDto
                .builder()
                .id(idPauta)
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(
                sessaoService.iniciarSessao(pautaDto, duracaoMinutos));

    }

    @PostMapping("/v1/pauta/{idPauta}/sessao/{idSessao}/votar")
    public ResponseEntity<Object> votar(
            @PathVariable Long idPauta,
            @PathVariable Long idSessao,
            @RequestBody VotoDto votoDto) {

        votoDto.setIdSessao(idSessao);
        sessaoService.votar(votoDto);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/v1/pauta/{idPauta}/sessao/{idSessao}/contabilizar")
    public ResponseEntity<Object> contabilizar(
            @PathVariable Long idPauta,
            @PathVariable Long idSessao) {

        return ResponseEntity.ok(
            sessaoService.contabilizarVotos(SessaoDto.builder().id(idSessao).build())
        );
    }

}
