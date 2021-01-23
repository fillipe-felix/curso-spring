package com.cursospring.cursomc.services;

import com.cursospring.cursomc.domain.Cliente;
import com.cursospring.cursomc.repositories.ClienteRepository;
import com.cursospring.cursomc.security.UserSpringSecurity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepository.findByEmail(email);
        if (cliente == null){
            throw new UsernameNotFoundException(email);
        }
        return new UserSpringSecurity(cliente.getId(), cliente.getEmail(), cliente.getSenha(),cliente.getPerfil());
    }
}
