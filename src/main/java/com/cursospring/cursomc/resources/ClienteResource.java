package com.cursospring.cursomc.resources;

import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.dto.ClienteDTO;
import com.cursospring.cursomc.dto.ClienteNewDTO;
import com.cursospring.cursomc.services.ClienteService;
import com.cursospring.cursomc.services.exceptions.AuthorizationException;
import com.cursospring.cursomc.services.exceptions.DataIntegrityException;
import com.cursospring.cursomc.services.exceptions.FileException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Cliente> findById(@PathVariable Integer id) throws ObjectNotFoundException, AuthorizationException {
        Cliente obj = clienteService.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @GetMapping("/email")
    public ResponseEntity<Cliente> find(@RequestParam(value = "value") String email) throws ObjectNotFoundException, AuthorizationException {
        Cliente obj = clienteService.finByEmail(email);
        return ResponseEntity.ok().body(obj);
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping
    public ResponseEntity<List<ClienteDTO>> findAll() {
        List<Cliente> clienteList = clienteService.findAll();
        //percorre a clienteList e transforma em um elemento categoriaDTOList para que nao retorne os produtos na
        // lista de clientes
        List<ClienteDTO> clienteDTOList =
                clienteList.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
        return ResponseEntity.ok().body(clienteDTOList);
    }

    @PostMapping
    public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDTO) {
        Cliente obj = clienteService.fromDTO(objDTO);
        obj = clienteService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).build();
    }

    //noContent para que retorne  http 204
    @PutMapping(value = "/{id}")
    public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDTO, @PathVariable Integer id) throws ObjectNotFoundException, AuthorizationException {
        Cliente obj = clienteService.fromDTO(objDTO);
        obj.setId(id);
        obj = clienteService.update(obj);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws ObjectNotFoundException, DataIntegrityException, AuthorizationException {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //cria o end-point para fazer a paginação das categorias
    //Faz a paginação no banco de dados com os parametros opcionais
    @GetMapping(value = "/page")
    public ResponseEntity<Page<ClienteDTO>> findPage(
            @RequestParam(value = "page", defaultValue = "0") Integer page,
            @RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage,
            @RequestParam(value = "orderBy", defaultValue = "nome") String orderBy,
            @RequestParam(value = "direction", defaultValue = "ASC") String direction) {


        Page<Cliente> clienteList = clienteService.findPage(page, linesPerPage, orderBy, direction);
        Page<ClienteDTO> clienteDTOList = clienteList.map(obj -> new ClienteDTO(obj));
        return ResponseEntity.ok().body(clienteDTOList);
    }

    @PostMapping("/picture")
    public ResponseEntity<Void> uploadProfilePicture(@RequestParam(name = "file")MultipartFile multipartFile) throws FileException, AuthorizationException {
        URI uri = clienteService.uploadProfilePicture(multipartFile);
        return ResponseEntity.created(uri).build();
    }
}
