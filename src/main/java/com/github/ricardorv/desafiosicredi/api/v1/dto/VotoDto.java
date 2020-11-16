package com.github.ricardorv.desafiosicredi.api.v1.dto;

import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class VotoDto {
    @ApiModelProperty(hidden = true)
    private Long idSessao;
    private VotoEnum voto;
    private String usuario;
    private String cpf;

}
