package Services;

import Enums.Categoria;
import Enums.Prioridade;
import Models.Solicitacao;
import Models.Usuario;
import Repositories.SolicitacaoRepository;

import java.util.List;

public class SolicitacaoService {
    private static final SolicitacaoRepository solicitacaoRepository = new SolicitacaoRepository();

    public void criarSolicitacao(String nome, String contato, String categoriaTexto, String descricao, String localizacao, String prioridadeTexto) {
        Categoria categoria = Categoria.valueOf(categoriaTexto.trim().toUpperCase());
        Prioridade prioridade = Prioridade.valueOf(prioridadeTexto.trim().toUpperCase());

        Usuario solicitante;
        if (nome == null || nome.trim().isEmpty()) {
            solicitante = new Usuario();
        } else {
            solicitante = new Usuario(nome.trim(), contato == null ? "" : contato.trim());
        }

        Solicitacao solicitacao = new Solicitacao(categoria, descricao, localizacao, prioridade, solicitante);
        solicitacaoRepository.salvar(solicitacao);

        System.out.println("Solicitação criada com sucesso! Protocolo: " + solicitacao.getProtocolo());
    }

    public void listarSolicitacoes() {
        List<Solicitacao> solicitacoes = solicitacaoRepository.listarTodas();

        if (solicitacoes.isEmpty()) {
            System.out.println("Nenhuma solicitação cadastrada.");
            return;
        }

        for (Solicitacao solicitacao : solicitacoes) {
            System.out.println("-------------------------------------");
            System.out.println("Protocolo: " + solicitacao.getProtocolo());
            System.out.println("Bairro/Local: " + solicitacao.getLocalizacao());
            System.out.println("Categoria: " + solicitacao.getCategoria());
            System.out.println("Status: " + solicitacao.getStatus());
            System.out.println("Título: " + solicitacao.getTitle());
            System.out.println("Descrição: " + solicitacao.getDescricao());
            System.out.println("Prioridade: " + solicitacao.getPrioridade());
            System.out.println("Solicitante: " + solicitacao.getSolicitante().getNome());
        }
        System.out.println("-------------------------------------");
    }
}
