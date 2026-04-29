package com.aep5s.models;

import com.aep5s.enums.StatusSolicitacao;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class HistoricoStatus{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String comentario;
    private String responsavel;
    private LocalDateTime dataHora;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusAnterior;

    @Enumerated(EnumType.STRING)
    private StatusSolicitacao statusNovo;

    public HistoricoStatus() {} // JPA

    public HistoricoStatus(StatusSolicitacao statusAnterior, StatusSolicitacao statusNovo, String comentario, String responsavel) {
        this.statusAnterior = statusAnterior;
        this.statusNovo = statusNovo;
        this.comentario = comentario;
        this.responsavel = responsavel;
        this.dataHora = LocalDateTime.now();
    }

    public String getComentario() {
        return comentario;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public StatusSolicitacao getStatusAnterior() {
        return statusAnterior;
    }

    public StatusSolicitacao getStatusNovo() {
        return statusNovo;
    }

    @Override
    public String toString() {
        return "[" + dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")) + "] "
                + statusAnterior + " → " + statusNovo
                + " | " + comentario
                + " | Resp: " + responsavel;
    }
}
