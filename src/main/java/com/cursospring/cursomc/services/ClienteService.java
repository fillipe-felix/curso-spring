package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Categoria;
import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.dto.CategoriaDTO;
import com.cursospring.cursomc.dto.ClienteDTO;
import com.cursospring.cursomc.repositories.ClienteRepository;
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
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente findById(Integer id) throws ObjectNotFoundException {
        Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
                ", Tipo: " + Cliente.class.getName()));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente update(Cliente obj) throws ObjectNotFoundException {
        Cliente newCliente = findById(obj.getId());
        updateData(newCliente, obj);
        return clienteRepository.save(newCliente);
    }

    private void updateData(Cliente newCliente, Cliente obj) {
        newCliente.setNome(obj.getNome());
        newCliente.setEmail(obj.getEmail());
    }

    public void delete(Integer id) throws ObjectNotFoundException, DataIntegrityException {
        findById(id);
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel excluir um cliente que possui pedidos");
        }
    }

    //Faz a paginação no banco de dados com os parametros opcionais
    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDTO){
        return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null);
    }

}
