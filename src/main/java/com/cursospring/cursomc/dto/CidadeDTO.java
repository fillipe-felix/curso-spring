package com.cursospring.cursomc.dto;

import com.cursospring.cursomc.domain.Cidade;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class CidadeDTO implements Serializable {

    private Integer id;
    private String nome;

    public CidadeDTO(Cidade obj) {
        id = obj.getId();
        nome = obj.getNome();
    }
}
