package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Cidade;
import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.domain.Endereco;
import com.cursospring.cursomc.domain.enums.Perfil;
import com.cursospring.cursomc.domain.enums.TipoCliente;
import com.cursospring.cursomc.dto.ClienteDTO;
import com.cursospring.cursomc.dto.ClienteNewDTO;
import com.cursospring.cursomc.repositories.ClienteRepository;
import com.cursospring.cursomc.repositories.EnderecoRepository;
import com.cursospring.cursomc.security.UserSpringSecurity;
import com.cursospring.cursomc.services.exceptions.AuthorizationException;
import com.cursospring.cursomc.services.exceptions.DataIntegrityException;
import com.cursospring.cursomc.services.exceptions.FileException;
import com.cursospring.cursomc.services.exceptions.ObjectNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private EnderecoRepository enderecoRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private S3Service s3Service;

    @Autowired
    private ImageService imageService;

    @Value("${img.prefix.client.profile}")
    private String prefix;

    @Value("${img.profile.size}")
    private int size;

    public Cliente findById(Integer id) throws ObjectNotFoundException, AuthorizationException {

        UserSpringSecurity user = UserService.authenticated();


        if (user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) {
            throw new AuthorizationException("Acesso negado");
        }

        Optional<Cliente> obj = clienteRepository.findById(id);
        return obj.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id +
                ", Tipo: " + Cliente.class.getName()));
    }

    public List<Cliente> findAll() {
        return clienteRepository.findAll();
    }

    public Cliente finByEmail(String email) throws AuthorizationException, ObjectNotFoundException {
        UserSpringSecurity user = UserService.authenticated();
        if (user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) {
            throw new AuthorizationException("Acesso negado");
        }

        Cliente obj = clienteRepository.findByEmail(email);
        if(obj == null){
            throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId() + " , Tipo: " + Cliente.class.getName());
        }
        return obj;
    }

    @Transactional
    public Cliente insert(Cliente obj) {
        obj.setId(null);
        obj = clienteRepository.save(obj);
        enderecoRepository.saveAll(obj.getEnderecos());
        return obj;
    }

    public Cliente update(Cliente obj) throws ObjectNotFoundException, AuthorizationException {
        Cliente newCliente = findById(obj.getId());
        updateData(newCliente, obj);
        return clienteRepository.save(newCliente);
    }

    private void updateData(Cliente newCliente, Cliente obj) {
        newCliente.setNome(obj.getNome());
        newCliente.setEmail(obj.getEmail());
    }

    public void delete(Integer id) throws ObjectNotFoundException, DataIntegrityException, AuthorizationException {
        findById(id);
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DataIntegrityException("Não é possivel excluir um cliente que possui pedidos");
        }
    }

    //Faz a paginação no banco de dados com os parametros opcionais
    public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction) {
        PageRequest pageRequest = PageRequest.of(page, linesPerPage, Sort.Direction.valueOf(direction), orderBy);
        return clienteRepository.findAll(pageRequest);
    }

    public Cliente fromDTO(ClienteDTO objDTO) {
        return new Cliente(objDTO.getId(), objDTO.getNome(), objDTO.getEmail(), null, null, null);
    }

    public Cliente fromDTO(ClienteNewDTO objDTO) {
        Cliente cliente = new Cliente(null, objDTO.getNome(), objDTO.getEmail(), objDTO.getCpfOuCnpj(),
                TipoCliente.toEnum(objDTO.getTipoCliente()), bCryptPasswordEncoder.encode(objDTO.getSenha()));
        Cidade cidade = new Cidade(objDTO.getCidadeId(), null, null);
        Endereco endereco = new Endereco(null, objDTO.getLogradouro(), objDTO.getNumero(), objDTO.getComplemento(),
                objDTO.getBairro(), objDTO.getCep(), cidade, cliente);
        cliente.getEnderecos().add(endereco);
        cliente.getTelefones().add(objDTO.getTelefone1());
        if (objDTO.getTelefone2() != null) {
            cliente.getTelefones().add(objDTO.getTelefone2());
        }
        if (objDTO.getTelefone3() != null) {
            cliente.getTelefones().add(objDTO.getTelefone3());
        }

        return cliente;
    }

    public URI uploadProfilePicture(MultipartFile multipartFile) throws FileException, AuthorizationException {

        UserSpringSecurity user = UserService.authenticated();

        if (user == null) {
            throw new AuthorizationException("Acesso Negado!");
        }

        BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
        jpgImage = imageService.cropSquare(jpgImage);
        jpgImage = imageService.resize(jpgImage, size);

        String fileName = prefix + user.getId() + ".jpg";

        return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
    }

}
