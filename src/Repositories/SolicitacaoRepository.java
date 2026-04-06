package Repositories;

import Enums.Categoria;
import Enums.Prioridade;
import Enums.StatusSolicitacao;
import Models.Solicitacao;
import Storage.SolicitacaoTxtStorage;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitacaoRepository implements SolicitacaoRepositoryInterface {
    private List<Solicitacao> solicitacoes = new ArrayList<>();
    // Implementação de arquivo extraída para reduzir responsabilidade do repository.
    private final SolicitacaoTxtStorage storage;

    public SolicitacaoRepository() {
        this.storage = new SolicitacaoTxtStorage();
    }

    public void salvar(Solicitacao solicitacao) {
        solicitacoes.add(solicitacao);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacoes.stream()
                .filter(s -> s.getProtocolo().equalsIgnoreCase(protocolo))
                .findFirst()
                .orElse(null);
    }

    public List<Solicitacao> listarTodas() {
        return solicitacoes;
    }

    // metodos divididos para separar responsabilidades
    public List<Solicitacao> filtrarPorStatus(StatusSolicitacao status) {
        return solicitacoes.stream()
                .filter(s -> s.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Solicitacao> filtrarPorCategoria(Categoria categoria) {
        return solicitacoes.stream()
                .filter(s -> s.getCategoria() == categoria)
                .collect(Collectors.toList());
    }

    public List<Solicitacao> filtrarPorLocalizacao(String localizacao) {
        return solicitacoes.stream()
                .filter(s -> s.getLocalizacao().equalsIgnoreCase(localizacao))
                .collect(Collectors.toList());
    }

    public List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade) {
        return solicitacoes.stream()
                .filter(s -> s.getPrioridade() == prioridade)
                .collect(Collectors.toList());
    }

    public void salvarEmArquivo(String nomeArquivo) {
        // Apenas delega a escrita: regra de negócio continua no service.
        storage.salvar(nomeArquivo, solicitacoes);
    }

    public int carregarDeArquivo(String nomeArquivo) {
        // Carrega do TXT e substitui a lista em memória compartilhada pelos menus.
        solicitacoes = storage.carregar(nomeArquivo);
        return solicitacoes.size();
    }
}
