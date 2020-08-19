package com.github.ricardorv.desafiosicredi.client;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfoDto {
    private StatusCpfEnum status;
}
