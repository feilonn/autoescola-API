package com.rc.autoescola.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.rc.autoescola.enums.TipoVeiculo;
import lombok.*;

import javax.persistence.*;
import java.util.Set;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Entity
public class Veiculo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(unique = true)
    @EqualsAndHashCode.Include
    private String placa;

    private String cor;

    private String modelo;

    private Integer ano;

    @Enumerated(EnumType.STRING)
    @EqualsAndHashCode.Include
    private TipoVeiculo tipoVeiculo;

    @OneToMany(mappedBy = "veiculo")
    @JsonIgnore
    private Set<Aluno> alunos;

    @PreRemove
    public void setVeiculoAsNullOnRemove() {
        for (Aluno aluno : getAlunos()) {
            if (aluno.getVeiculo() != null && aluno.getVeiculo().equals(this)) {
                aluno.setVeiculo(null);
            }
        }
    }
}
