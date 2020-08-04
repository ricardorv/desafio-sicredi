package com.github.ricardorv.desafiosicredi.service;

import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;

import javax.persistence.EntityNotFoundException;
import java.io.IOException;

public interface AssociadoService {

    Boolean podeVotar(String cpf) throws CpfInvalidoException, EntityNotFoundException;

}
