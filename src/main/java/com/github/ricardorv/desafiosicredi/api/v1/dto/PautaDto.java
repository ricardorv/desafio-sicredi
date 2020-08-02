package com.github.ricardorv.desafiosicredi.api.v1.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PautaDto {
    @ApiModelProperty(hidden = true)
    private Long id;
    private String nome;
}
