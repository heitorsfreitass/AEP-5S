package Repositories;

import Enums.Categoria;
import Enums.StatusSolicitacao;
import Models.Solicitacao;
import Enums.Prioridade;

import java.util.List;

public interface SolicitacaoRepositoryInterface {

    void salvar(Solicitacao solicitacao);

    Solicitacao buscarPorProtocolo(String protocolo);

    List<Solicitacao> listarTodas();

    List<Solicitacao> filtrarPorStatus(StatusSolicitacao status);

    List<Solicitacao> filtrarPorCategoria(Categoria categoria);

    List<Solicitacao> filtrarPorLocalizacao(String localizacao);

    List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade);

    void salvarEmArquivo(String nomeArquivo);

    int carregarDeArquivo(String nomeArquivo);
}