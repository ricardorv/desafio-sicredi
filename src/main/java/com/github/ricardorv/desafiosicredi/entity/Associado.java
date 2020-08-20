package com.github.ricardorv.desafiosicredi.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Associado {

    @Id
    @GeneratedValue
    private Long id;
    @Column(unique = true)
    private String cpf;
    private String usuario;

}
