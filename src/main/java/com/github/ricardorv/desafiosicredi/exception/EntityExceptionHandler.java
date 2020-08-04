package com.github.ricardorv.desafiosicredi.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.persistence.EntityNotFoundException;

@ControllerAdvice
public class EntityExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value = {EntityNotFoundException.class})
    protected ResponseEntity<Object> handleEntityNotFound(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = ex.getMessage();
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { SessaoJaExpirouException.class })
    protected ResponseEntity<Object> handleSessaoJaExpirou(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Sessão já expirou";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { SessaoJaIniciadaException.class })
    protected ResponseEntity<Object> handleSessaoJaIniciada(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Sessão já foi iniciada";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { VotoJaComputadoException.class })
    protected ResponseEntity<Object> handleVotoJaComputado(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "Voto já foi computado";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler(value = { CpfInvalidoException.class })
    protected ResponseEntity<Object> handleCpfInvalido(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "CPF Inválido";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler(value = { CpfNaoPodeVotarException.class })
    protected ResponseEntity<Object> handleCpfNaoPodeVotar(RuntimeException ex, WebRequest request) {
        String bodyOfResponse = "CPF não pode votar";
        return handleExceptionInternal(ex, bodyOfResponse,
                new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

}