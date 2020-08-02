package com.github.ricardorv.desafiosicredi.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AssociadoDto {
    private Long id;
    private String cpf;
    private String token;
}
