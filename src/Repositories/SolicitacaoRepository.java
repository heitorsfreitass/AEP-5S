package Repositories;

import Enums.Categoria;
import Enums.StatusSolicitacao;
import Models.Solicitacao;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class SolicitacaoRepository {
    private List<Solicitacao> solicitacoes = new ArrayList<>();

    public void salvar(Solicitacao solicitacao)
    {
        solicitacoes.add(solicitacao);
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        Solicitacao resultado = null;
        for (int i = 0; i < solicitacoes.size(); i++) {
            if (solicitacoes.get(i).getProtocolo().equalsIgnoreCase(protocolo)) {
                resultado = solicitacoes.get(i);
            }
        }
        return resultado;
    }

    public List<Solicitacao> listarTodas()
    {
        return solicitacoes;
    }

    public List<Solicitacao> filtrarPorStatusECategoria(StatusSolicitacao status, Categoria categoria) {
        List<Solicitacao> resultado = new ArrayList<>();
        for (Solicitacao s : solicitacoes) {
            if (s.getStatus() == status && s.getCategoria() == categoria) {
                resultado.add(s);
            }
        }
        return resultado;
    }

    //-- Dar um jeito de não usar loop for com if
    //-- Separar responsabilidades do filtrarPorStatusECategoria()

    public List<Solicitacao> filtrarPorLocalizacao(String localizacao)
    {
        return solicitacoes.stream()
                .filter(s -> s.getLocalizacao().equalsIgnoreCase(localizacao))
                .collect(Collectors.toList());
    }
}
