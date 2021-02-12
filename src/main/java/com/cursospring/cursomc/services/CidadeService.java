package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Cidade;
import com.cursospring.cursomc.repositories.CidadeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CidadeService {

    @Autowired
    private CidadeRepository cidadeRepository;

    public List<Cidade> findByEstado(Integer estadoId){
        return cidadeRepository.findcidades(estadoId);
    }
}
