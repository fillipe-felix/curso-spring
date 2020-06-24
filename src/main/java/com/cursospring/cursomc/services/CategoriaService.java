package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Categoria;
import com.cursospring.cursomc.dto.CategoriaDTO;
import com.cursospring.cursomc.repositories.CategoriaRepository;
import com.cursospring.cursomc.services.exceptions.DataIntegrityException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
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

    public List<Categoria> findAll() {
        return categoriaRepository.findAll();
    }

    public Categoria insert(Categoria obj) {
        obj.setId(null);
        return categoriaRepository.save(obj);
    }

    public Categoria update(Categoria obj) throws ObjectNotFoundException {
        Categoria newCategoria = findById(obj.getId());
        updateData(newCategoria, obj);
        return categoriaRepository.save(newCategoria);
    }

    private void updateData(Categoria categoria, Categoria obj) {
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

    //Faz a paginação no banco de dados com os parametros opcionais
    public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return categoriaRepository.findAll(pageRequest);
    }

    public Categoria fromDTO(CategoriaDTO objDTO){
        return new Categoria(objDTO.getId(), objDTO.getNome());
    }
}
