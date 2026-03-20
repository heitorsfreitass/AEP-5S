package Models;

import Enums.Categoria;
import Enums.Prioridade;
import Enums.StatusSolicitacao;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Solicitacao {
    private String protocolo;
    private String descricao;
    private String localizacao;
    private LocalDateTime dataAbertura;
    private LocalDateTime prazoAlvo;
    private Categoria categoria;
    private Prioridade prioridade;
    private StatusSolicitacao status;
    private Usuario solicitante;
    private List<HistoricoStatus> historico;

    public Solicitacao(Categoria categoria, String descricao, String localizacao, Prioridade prioridade, Usuario solicitante) {
        this.protocolo = "OA-" + LocalDateTime.now().getYear() + "-" + java.util.UUID.randomUUID().toString().substring(0, 6).toUpperCase();
        this.categoria = categoria;
        this.descricao = descricao;
        this.localizacao = localizacao;
        this.prioridade = prioridade;
        this.solicitante = solicitante;
        this.status = StatusSolicitacao.ABERTO;
        this.dataAbertura = LocalDateTime.now();
        this.historico = new ArrayList<>();
    }

    public void atualizarStatus(StatusSolicitacao novoStatus, String comentario, String responsavel) {
        historico.add(new HistoricoStatus(this.status, novoStatus, comentario, responsavel));
        this.status = novoStatus;
    }


}
