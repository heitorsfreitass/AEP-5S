package com.aep5s.enums;

public enum Categoria {
    ILUMINACAO("Iluminação Pública"),
    BURACO("Buraco na Via"),
    LIMPEZA("Limpeza Urbana"),
    SAUDE("Saúde"),
    PODA("Poda de Árvore"),
    VAZAMENTO("Vazamento de Água"),
    ASSEDIO("Assédio / Denúncia"),
    SEGURANCA("Segurança Escolar"),
    OUTRO("Outro");

    private final String descricao;

    Categoria(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
