package com.cursospring.cursomc.dto;

import com.cursospring.cursomc.domain.Estado;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class EstadoDTO implements Serializable {

    private Integer id;
    private String nome;

    public EstadoDTO(Estado obj) {
        id = obj.getId();
        nome = obj.getNome();
    }
}
