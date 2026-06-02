package com.aep5s.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    
    @GetMapping("/")
    public String login() {
        return "login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @GetMapping("/solicitacao")
    public String solicitacao() {
        return "solicitacao";
    }

    @GetMapping("/protocolo")
    public String protocolo() {
        return "protocolo";
    }

    @GetMapping("/servidor")
    public String servidor() {
        return "servidor";
    }

    @GetMapping("/sla")
    public String sla() {
        return "sla";
    }
}
