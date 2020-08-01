package com.github.ricardorv.desafiosicredi.entity;

import com.github.ricardorv.desafiosicredi.enums.VotoEnum;
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
public class Voto {

    @Id
    @GeneratedValue
    private Long id;
    private LocalDateTime dthrInserido;
    @Enumerated(EnumType.STRING)
    private VotoEnum voto;
    @ManyToOne
    private Sessao sessao;
    @ManyToOne
    private Associado associado;

}
