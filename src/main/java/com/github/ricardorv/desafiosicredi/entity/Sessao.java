package com.github.ricardorv.desafiosicredi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Sessao {

    @Id
    @GeneratedValue
    private  Long id;
    private Integer duracaoMinutos;
    private LocalDateTime inicioSessao;
    private LocalDateTime fimSessao;
    @ManyToOne
    private Pauta pauta;

}
