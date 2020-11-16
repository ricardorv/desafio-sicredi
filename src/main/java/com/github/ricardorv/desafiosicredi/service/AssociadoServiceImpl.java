package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.client.UserInfoClient;
import com.github.ricardorv.desafiosicredi.client.UserInfoDto;
import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    private final UserInfoClient userInfoClient;

    public AssociadoServiceImpl(UserInfoClient userInfoClient) {
        this.userInfoClient = userInfoClient;
    }

    @Override
    public Boolean podeVotar(String cpf) throws CpfInvalidoException {
        try {
            UserInfoDto user = userInfoClient.getUser(cpf);
            StatusCpfEnum status = user.getStatus();
            return StatusCpfEnum.ABLE_TO_VOTE.equals(status);
        } catch (EntityNotFoundException ex) {
            throw new CpfInvalidoException();
        }
    }
}
