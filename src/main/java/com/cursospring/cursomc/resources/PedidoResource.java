package com.cursospring.cursomc.resources;

import com.cursospring.cursomc.domain.Categoria;
import com.cursospring.cursomc.domain.Pedido;
import com.cursospring.cursomc.dto.CategoriaDTO;
import com.cursospring.cursomc.services.PedidoService;
import com.cursospring.cursomc.services.exceptions.AuthorizationException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;

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

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj) throws ObjectNotFoundException, AuthorizationException {
        obj = pedidoService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    @GetMapping()
    public ResponseEntity<Page<Pedido>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "instante") String orderBy,
            @RequestParam(value = "direction", defaultValue = "DESC") String direction) throws ObjectNotFoundException, AuthorizationException {


        Page<Pedido> pedidoList = pedidoService.findPage(page, linesPerPage, orderBy, direction);
        return ResponseEntity.ok().body(pedidoList);
    }
}
