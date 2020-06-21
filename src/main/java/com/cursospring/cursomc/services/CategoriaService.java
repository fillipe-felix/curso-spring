package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Categoria;
import com.cursospring.cursomc.repositories.CategoriaRepository;
import com.cursospring.cursomc.services.exceptions.DataIntegrityException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    public Categoria findById(Integer id) throws ObjectNotFoundException {
        Optional<Categoria> obj = categoriaRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException(
                "Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return categoriaRepository.save(obj);
    }

    public Categoria update(Integer id, Categoria obj) throws ObjectNotFoundException {
        findById(id);
        Categoria categoria = categoriaRepository.getOne(id);
        updateData(categoria, obj);
        return categoriaRepository.save(categoria);
    }

    private void updateData(Categoria categoria, Categoria obj) {
        //categoria.setId(obj.getId());
        categoria.setNome(obj.getNome());
    }

    public void delete(Integer id) throws ObjectNotFoundException, DataIntegrityException {
        findById(id);
        try {
            categoriaRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel excluir uma categoria que possui produtos");
        }
    }
}
