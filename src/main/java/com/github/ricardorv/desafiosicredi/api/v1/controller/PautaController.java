package com.github.ricardorv.desafiosicredi.api.v1.controller;

import com.github.ricardorv.desafiosicredi.api.v1.dto.PautaDto;
import com.github.ricardorv.desafiosicredi.service.PautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class PautaController {

    private PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @GetMapping("/v1/pauta")
    public ResponseEntity<List<PautaDto>> all() {
        try {
            return ResponseEntity.ok(pautaService.buscarPautas());
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/v1/pauta/{id}")
    public ResponseEntity<PautaDto> one(@PathVariable Long id) {
        try {
            return ResponseEntity.ok(pautaService.buscarPauta(id));
        } catch (Exception ex) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/v1/pauta")
    public ResponseEntity<PautaDto> newPauta(@RequestBody PautaDto pauta) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(
                    pautaService.cadastrarPauta(pauta.getNome()));
        } catch (Exception ex) {
            return ResponseEntity.badRequest().build();
        }
    }

}
