package com.aep5s.services;

import com.aep5s.models.Usuario;
import com.aep5s.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario criarIdentificado(String nome, String contato) {
        Usuario usuario = new Usuario(nome.trim(), contato == null ? "" : contato.trim());

        return usuarioRepository.save(usuario);
    }

    public Usuario criarAnonimo() {
        Usuario usuario = Usuario.anonimo();

        return usuarioRepository.save(usuario);
    }
}