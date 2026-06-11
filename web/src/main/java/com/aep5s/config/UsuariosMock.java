package com.aep5s.config;

import com.aep5s.models.UsuarioSessao;
import com.aep5s.models.UsuarioSessao.Papel;

import java.util.Map;

public class UsuariosMock {

    // login -> { senha, nomeExibicao, papel }
    private static final Map<String, Object[]> USUARIOS = Map.of(
        "joao",    new Object[]{ "123",      "João Silva",        Papel.CIDADAO },
        "maria",   new Object[]{ "123",      "Maria Souza",       Papel.CIDADAO },
        "carlos",  new Object[]{ "123",      "Carlos Lima",       Papel.CIDADAO },
        "admin",   new Object[]{ "admin123", "Administrador",     Papel.ADMIN   },
        "gestor",  new Object[]{ "admin123", "Gestor Municipal",  Papel.ADMIN   }
    );

    public static UsuarioSessao autenticar(String login, String senha) {
        Object[] dados = USUARIOS.get(login == null ? "" : login.trim().toLowerCase());
        if (dados == null) return null;
        if (!dados[0].equals(senha)) return null;
        return new UsuarioSessao(login.trim().toLowerCase(), (String) dados[1], (Papel) dados[2]);
    }

    private UsuariosMock() {}
}
