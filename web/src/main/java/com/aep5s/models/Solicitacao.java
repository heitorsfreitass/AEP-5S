package com.aep5s.models;

import com.aep5s.enums.Categoria;
import com.aep5s.enums.Prioridade;
import com.aep5s.enums.StatusSolicitacao;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Solicitacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String protocolo;
    private String title;
    private String descricao;
    private String localizacao;
    private LocalDateTime dataAbertura;
    private LocalDateTime prazoAlvo;

    @Enumerated(EnumType.STRING)
    private Categoria categoria;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao status;

    private Usuario solicitante;
    private List<HistoricoStatus> historico;

    public Solicitacao() {} // JPA

    public Solicitacao(Categoria categoria, String descricao, String localizacao, Prioridade prioridade, Usuario solicitante) {
        this.protocolo = "OA-" + LocalDateTime.now().getYear() + "-" + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.prioridade = prioridade;
        this.solicitante = solicitante;
        this.status = StatusSolicitacao.ABERTO;
        this.dataAbertura = LocalDateTime.now();
        this.prazoAlvo = this.dataAbertura.plusHours(prioridade.getSlaHoras());
        this.historico = new ArrayList<>();
    }

    public void atualizarStatus(StatusSolicitacao novoStatus, String comentario, String responsavel) {
        historico.add(new HistoricoStatus(this.status, novoStatus, comentario, responsavel));
        this.status = novoStatus;
    }

    public String getProtocolo() {
        return protocolo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocalizacao() {
        return localizacao;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public Prioridade getPrioridade() {
        return prioridade;
    }

    public StatusSolicitacao getStatus() {
        return status;
    }

    public Usuario getSolicitante() {
        return solicitante;
    }

    public LocalDateTime getDataAbertura() {
        return dataAbertura;
    }

    public LocalDateTime getPrazoAlvo() {
        return prazoAlvo;
    }

    public List<HistoricoStatus> getHistorico() {
        return historico;
    }
    
    
}
