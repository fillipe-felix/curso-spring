package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.domain.Pedido;
import com.cursospring.cursomc.repositories.PedidoRepository;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    public Pedido findById(Integer id) throws ObjectNotFoundException {
        Optional<Pedido> obj = pedidoRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
                ", Tipo: " + Pedido.class.getName()));
    }
}
