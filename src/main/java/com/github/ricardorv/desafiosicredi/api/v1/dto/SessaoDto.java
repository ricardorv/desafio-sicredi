package com.github.ricardorv.desafiosicredi.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessaoDto {

    private Long id;
    private Integer duracaoMinutos;
    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;

}
