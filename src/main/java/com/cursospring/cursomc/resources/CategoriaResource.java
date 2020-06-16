package com.cursospring.cursomc.resources;

import com.cursospring.cursomc.domain.Categoria;
import com.cursospring.cursomc.services.CategoriaService;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<?> find(@PathVariable Integer id) throws ObjectNotFoundException {


        Categoria obj = categoriaService.find(id);

        return ResponseEntity.ok().body(obj);
    }
}
