package com.github.ricardorv.desafiosicredi.client;

import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoDto {
    private StatusCpfEnum status;
}
