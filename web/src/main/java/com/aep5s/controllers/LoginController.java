package com.aep5s.controllers;

import com.aep5s.config.UsuariosMock;
import com.aep5s.models.UsuarioSessao;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {

    @GetMapping("/login")
    public String paginaLogin(@RequestParam(required = false) String erro,
                               @RequestParam(required = false) String next,
                               Model model) {
        if (erro != null) {
            String msg = switch (erro) {
                case "credenciais" -> "Login ou senha incorretos.";
                case "permissao"   -> "Acesso restrito a administradores.";
                case "acesso"      -> "Faça login para continuar.";
                default            -> "Erro de autenticação.";
            };
            model.addAttribute("erroMsg", msg);
        }
        model.addAttribute("next", next);
        return "login";
    }

    @PostMapping("/login")
    public String efetuarLogin(@RequestParam String login,
                                @RequestParam String senha,
                                @RequestParam(required = false) String next,
                                HttpSession session) {
        UsuarioSessao usuario = UsuariosMock.autenticar(login, senha);

        if (usuario == null) {
            return "redirect:/login?erro=credenciais";
        }

        session.setAttribute("usuarioLogado", usuario);

        if (next != null && !next.isBlank()) {
            return "redirect:" + next;
        }
        return usuario.isAdmin() ? "redirect:/servidor" : "redirect:/";
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
