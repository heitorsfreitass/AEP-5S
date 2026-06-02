package com.aep5s.controllers;

import com.aep5s.models.Usuario;
import com.aep5s.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

@Controller
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/usuarios")
    public String home(Model model) {
        model.addAttribute("usuarios", usuarioService.getAll());
        return "usuarios";
    }

    @PostMapping("/usuarios/anonimo")
    public String criarAnonimo() {
        usuarioService.criarAnonimo();
        return "redirect:/usuarios";
    }

    @PostMapping("/usuarios/identificado")
    public String criarIdentificado(String nome, String contato) {
        if (nome == null || nome.trim().isEmpty()) {
            return "redirect:/usuarios";
        }
        usuarioService.criarIdentificado(nome, contato);
        return "redirect:/usuarios";
    }
}
