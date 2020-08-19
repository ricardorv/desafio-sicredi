package com.github.ricardorv.desafiosicredi.client;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.ricardorv.desafiosicredi.enums.StatusCpfEnum;
import com.github.ricardorv.desafiosicredi.exception.CpfInvalidoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.persistence.EntityNotFoundException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Service
public class UserInfoClient {

    public UserInfoDto getUser(String cpf) {
        RestTemplate restTemplate = new RestTemplate();
        String resourceUrl = "https://user-info.herokuapp.com/users/";
        ResponseEntity<UserInfoDto> response = restTemplate.getForEntity(resourceUrl.concat(cpf), UserInfoDto.class);

        if (response.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
            throw new CpfInvalidoException();
        }

        return response.getBody();
    }



}
