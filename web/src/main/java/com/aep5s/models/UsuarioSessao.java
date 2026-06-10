package com.aep5s.models;

public class UsuarioSessao {

    public enum Papel { CIDADAO, ADMIN }

    private final String login;
    private final String nomeExibicao;
    private final Papel papel;

    public UsuarioSessao(String login, String nomeExibicao, Papel papel) {
        this.login = login;
        this.nomeExibicao = nomeExibicao;
        this.papel = papel;
    }

    public String getLogin()          { return login; }
    public String getNomeExibicao()   { return nomeExibicao; }
    public Papel  getPapel()          { return papel; }
    public boolean isAdmin()          { return papel == Papel.ADMIN; }
}
