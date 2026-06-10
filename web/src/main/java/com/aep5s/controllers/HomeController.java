package com.aep5s.controllers;

// Rotas principais agora estão em SolicitacaoController.
// Este arquivo mantido apenas para o redirect de /dashboard -> /
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/dashboard")
    public String dashboard() {
        return "redirect:/";
    }
}
