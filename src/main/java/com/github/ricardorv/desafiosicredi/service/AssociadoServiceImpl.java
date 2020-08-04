package com.github.ricardorv.desafiosicredi.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class AssociadoServiceImpl implements AssociadoService {

    public AssociadoServiceImpl() {
    }

    @Override
    public Boolean podeVotar(String cpf) throws CpfInvalidoException, EntityNotFoundException {
        BufferedReader in = null;
        try {
            URL url = new URL("https://user-info.herokuapp.com/users/".concat(cpf));
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            int httpStatus = con.getResponseCode();
            if (HttpStatus.NOT_FOUND.value() == httpStatus) {
                throw new CpfInvalidoException();
            }

            in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            JsonNode parent = new ObjectMapper().readTree(content.toString());
            String status = parent.get("status").asText();

            return StatusCpfEnum.ABLE_TO_VOTE.toString().equals(status);
        } catch (IOException e) {
            throw new EntityNotFoundException(e.getMessage());
        } finally {
            try {
                in.close();
            } catch (Exception ex) {
            }
        }
    }
}
