package Services;

import Enums.Prioridade;
import Enums.StatusSolicitacao;
import Models.Solicitacao;
import Repositories.SolicitacaoRepositoryInterface;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;

public class FilaAtendimentoService {
    private static final EnumSet<StatusSolicitacao> STATUS_ATIVOS = EnumSet.of(
            StatusSolicitacao.ABERTO,
            StatusSolicitacao.TRIAGEM,
            StatusSolicitacao.EM_EXECUCAO
    );


    private final SolicitacaoRepositoryInterface solicitacaoRepository;

    public FilaAtendimentoService(SolicitacaoRepositoryInterface solicitacaoRepository) {
        this.solicitacaoRepository = Objects.requireNonNull(solicitacaoRepository, "Repositorio de solicitacoes nao pode ser nulo.");
    }

    public List<Solicitacao> listarOrdenadoPorPrioridade() {
        List<Solicitacao> ordenadas = new ArrayList<>(solicitacaoRepository.listarTodas());
        ordenadas.sort((a, b) -> Integer.compare(pesoPrioridade(b.getPrioridade()), pesoPrioridade(a.getPrioridade())));
        return ordenadas;
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) {
        List<Solicitacao> filtradas = new ArrayList<>();

        for (Solicitacao solicitacao : solicitacaoRepository.listarTodas()) {
            if (solicitacao.getStatus() == status) {
                filtradas.add(solicitacao);
            }
        }

        return filtradas;
    }

    public List<Solicitacao> listarFilaPorSla() {
        List<Solicitacao> ativas = new ArrayList<>();

        for (Solicitacao solicitacao : solicitacaoRepository.listarTodas()) {
            if (STATUS_ATIVOS.contains(solicitacao.getStatus())) {
                ativas.add(solicitacao);
            }
        }

        ativas.sort((a, b) -> {
            int comparacaoPrazo = a.getPrazoAlvo().compareTo(b.getPrazoAlvo());
            if (comparacaoPrazo != 0) {
                return comparacaoPrazo;
            }
            return Integer.compare(pesoPrioridade(b.getPrioridade()), pesoPrioridade(a.getPrioridade()));
        });

        return ativas;
    }

    private int pesoPrioridade(Prioridade prioridade) {
        switch (prioridade) {
            case URGENTE:
                return 4;
            case ALTA:
                return 3;
            case MEDIA:
                return 2;
            case BAIXA:
            default:
                return 1;
        }
    }
}

