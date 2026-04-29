package com.aep5s.models;

import jakarta.persistence.*;

@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;
    private String contato;
    private boolean anonimo;

    //--- 2 Construtores pq o anonimo eh setado na criação e não pode mudar depois
    //-- 1 pra anonimo = true e um pra anonimo = false

    public Usuario() {} // JPA

    // Usuário nao anonimo
    public Usuario(String nome, String contato) {
        this.nome = nome;
        this.contato = contato;
        this.anonimo = false;
    }

    // Usuário anônimo
    public static Usuario anonimo() {
        Usuario usuario = new Usuario();

        usuario.nome = "Anônimo";
        usuario.contato = null;
        usuario.anonimo = true;

        return usuario;
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getContato() {
        return contato;
    }

    public void setContato(String contato) {
        this.contato = contato;
    }

    public boolean isAnonimo() {
        return anonimo;
    }
}
