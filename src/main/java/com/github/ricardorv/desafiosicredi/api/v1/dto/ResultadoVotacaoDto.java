package com.github.ricardorv.desafiosicredi.api.v1.dto;

import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultadoVotacaoDto implements Serializable {

    Map<VotoEnum, Long> quantidadeVotos;
    LocalDateTime encerramento;

}
