package Models;

import Enums.StatusSolicitacao;

import java.time.LocalDateTime;

public class HistoricoStatus{
    private String comentario;
    private String responsavel;
    private LocalDateTime dataHora;
    private StatusSolicitacao statusAnterior;
    private StatusSolicitacao statusNovo;

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
