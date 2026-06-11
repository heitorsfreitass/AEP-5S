package com.aep5s.config;

import com.aep5s.models.UsuarioSessao;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAttributes {

    @ModelAttribute("usuarioLogado")
    public UsuarioSessao usuarioLogado(HttpSession session) {
        return (UsuarioSessao) session.getAttribute("usuarioLogado");
    }
}
