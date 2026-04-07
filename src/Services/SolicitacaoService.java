package Services;

import Enums.Categoria;
import Enums.Prioridade;
import Enums.StatusSolicitacao;
import Models.Solicitacao;
import Models.Usuario;
import Repositories.SolicitacaoRepositoryInterface;

import java.text.Normalizer;
import java.util.List;

public class SolicitacaoService {

    private final SolicitacaoRepositoryInterface solicitacaoRepository;
    private final UsuarioService usuarioService = new UsuarioService();
    private final FilaAtendimentoService filaAtendimentoService;

    public SolicitacaoService(SolicitacaoRepositoryInterface solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.filaAtendimentoService = new FilaAtendimentoService(solicitacaoRepository);
    }

    // retorna o protocolo gerado para a UI exibir
    public String criarSolicitacao(String nome, String contato, String categoriaTexto,
                                   String titulo, String descricao,
                                   String localizacao, String prioridadeTexto) {
        Categoria categoria = parseCategoria(categoriaTexto);
        Prioridade prioridade = Prioridade.valueOf(prioridadeTexto.trim().toUpperCase());

        // delega criacao do usuario pro UsuarioService
        Usuario solicitante;
        if (nome == null || nome.trim().isEmpty()) {
            solicitante = usuarioService.criarAnonimo();
        } else {
            solicitante = usuarioService.criarIdentificado(nome, contato);
        }

        Solicitacao solicitacao = new Solicitacao(categoria, descricao, localizacao, prioridade, solicitante);
        solicitacao.setTitle(titulo == null ? "" : titulo.trim());
        solicitacaoRepository.salvar(solicitacao);
        return solicitacao.getProtocolo();
    }

    // so retorna a lista - quem exibe e a UI
    public List<Solicitacao> listarSolicitacoes() {
        return solicitacaoRepository.listarTodas();
    }

    public List<Solicitacao> listarFilaAtendimentoSla() {
        return filaAtendimentoService.listarFilaPorSla();
    }


    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacaoRepository.buscarPorProtocolo(protocolo);
    }

    public void atualizarStatus(String protocolo, String novoStatusTexto,
                                String comentario, String responsavel) {
        StatusSolicitacao novoStatus = StatusSolicitacao.valueOf(novoStatusTexto.trim().toUpperCase());
        Solicitacao solicitacao = solicitacaoRepository.buscarPorProtocolo(protocolo);
        solicitacao.atualizarStatus(novoStatus, comentario, responsavel);
    }

    public List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade) {
        return solicitacaoRepository.filtrarPorPrioridade(prioridade);
    }

    public List<Solicitacao> filtrarPorCategoria(Categoria categoria) {
        return solicitacaoRepository.filtrarPorCategoria(categoria);
    }

    public List<Solicitacao> filtrarPorBairro(String bairro) {
        return solicitacaoRepository.filtrarPorLocalizacao(bairro);
    }

    public int carregarSolicitacoes(String nomeArquivo) {
        return solicitacaoRepository.carregarDeArquivo(nomeArquivo);
    }

    public void salvarSolicitacoes(String nomeArquivo) {
        solicitacaoRepository.salvarEmArquivo(nomeArquivo);
    }

    private Categoria parseCategoria(String categoriaTexto) {
        if (categoriaTexto == null) {
            throw new IllegalArgumentException("Categoria invalida");
        }

        String normalizada = Normalizer.normalize(categoriaTexto, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .trim()
                .toUpperCase()
                .replace(' ', '_');

        return Categoria.valueOf(normalizada);
    }
}