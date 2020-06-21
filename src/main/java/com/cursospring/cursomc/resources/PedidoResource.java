package com.cursospring.cursomc.resources;

import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.domain.Pedido;
import com.cursospring.cursomc.services.ClienteService;
import com.cursospring.cursomc.services.PedidoService;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

    @Autowired
    private PedidoService pedidoService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Pedido> findById(@PathVariable Integer id) throws ObjectNotFoundException {
        Pedido obj = pedidoService.findById(id);
        return ResponseEntity.ok().body(obj);
    }
}
