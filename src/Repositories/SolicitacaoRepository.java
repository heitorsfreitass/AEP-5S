package Repositories;

import Enums.Categoria;
import Enums.StatusSolicitacao;
import Models.Solicitacao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitacaoRepository implements SolicitacaoRepositoryInterface{
    private List<Solicitacao> solicitacoes = new ArrayList<>();

    public void salvar(Solicitacao solicitacao)
    {
        solicitacoes.add(solicitacao);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacoes.stream()
                .filter(s -> s.getProtocolo().equalsIgnoreCase(protocolo))
                .findFirst()
                .orElse(null);
    }

    public List<Solicitacao> listarTodas()
    {
        return solicitacoes;
    }

    //métodos divididos para separar responsabilidades
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
}
