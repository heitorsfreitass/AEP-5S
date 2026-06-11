package com.aep5s.services;

import com.aep5s.enums.Categoria;
import com.aep5s.enums.Prioridade;
import com.aep5s.enums.StatusSolicitacao;
import com.aep5s.models.Solicitacao;
import com.aep5s.models.Usuario;
import com.aep5s.repositories.SolicitacaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Normalizer;
import java.util.List;

@Service
public class SolicitacaoService {

    private final SolicitacaoRepository solicitacaoRepository;
    private final UsuarioService usuarioService;
    private final FilaAtendimentoService filaAtendimentoService;

    public SolicitacaoService(SolicitacaoRepository solicitacaoRepository, UsuarioService usuarioService, FilaAtendimentoService filaAtendimentoService) {
        this.solicitacaoRepository = solicitacaoRepository;
        this.usuarioService = usuarioService;
        this.filaAtendimentoService = filaAtendimentoService;
    }

    // retorna o protocolo gerado para a UI exibir
    public String criarSolicitacao(String nome, String contato, String categoriaTexto,
                                   String titulo, String descricao,
                                   String localizacao, String prioridadeTexto) {
        Categoria categoria = parseCategoria(categoriaTexto);
        Prioridade prioridade = parsePrioridade(prioridadeTexto);

        // delega criacao do usuario pro UsuarioService
        Usuario solicitante = (nome == null || nome.trim().isEmpty())
                ? usuarioService.criarAnonimo()
                : usuarioService.criarIdentificado(nome, contato);

        Solicitacao solicitacao = new Solicitacao(categoria, descricao, localizacao, prioridade, solicitante);
        solicitacao.setTitle(titulo == null ? "" : titulo.trim());
        solicitacaoRepository.save(solicitacao);
        return solicitacao.getProtocolo();
    }

    // so retorna a lista - quem exibe e a UI
    public List<Solicitacao> listarSolicitacoes() {
        return solicitacaoRepository.findAll();
    }

    public List<Solicitacao> listarFilaAtendimentoSla() {
        return filaAtendimentoService.listarFilaPorSla();
    }

    public Solicitacao buscarPorProtocolo(String protocolo) {
        return solicitacaoRepository.findByProtocolo(protocolo);
    }

    public void atualizarStatus(String protocolo, String novoStatusTexto,
                                String comentario, String responsavel) {
        StatusSolicitacao novoStatus = StatusSolicitacao.valueOf(novoStatusTexto.trim().toUpperCase());
        Solicitacao solicitacao = solicitacaoRepository.findByProtocolo(protocolo);

        if (solicitacao == null) {
            throw new RuntimeException("Solicitação não encontrada");
        }

        solicitacao.atualizarStatus(novoStatus, comentario, responsavel);
        solicitacaoRepository.save(solicitacao);
    }

    public List<Solicitacao> filtrarPorPrioridade(Prioridade prioridade) {
        return solicitacaoRepository.findByPrioridade(prioridade);
    }

    public List<Solicitacao> filtrarPorCategoria(Categoria categoria) {
        return solicitacaoRepository.findByCategoria(categoria);
    }

    public List<Solicitacao> filtrarPorBairro(String bairro) {
        return solicitacaoRepository.findByLocalizacaoIgnoreCase(bairro);
    }

    private Categoria parseCategoria(String categoriaTexto) {
        if (categoriaTexto == null) {
            throw new IllegalArgumentException("Categoria invalida");
        }

        String entrada = categoriaTexto.trim();
        if (entrada.isEmpty()) {
            throw new IllegalArgumentException("Categoria invalida");
        }

        try {
            int indice = Integer.parseInt(entrada);
            Categoria[] categorias = Categoria.values();
            if (indice >= 1 && indice <= categorias.length) {
                return categorias[indice - 1];
            }
        } catch (NumberFormatException ignored) {
            // Se nao for numero, tenta por nome do enum.
        }

        String normalizada = Normalizer.normalize(entrada, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")
                .toUpperCase()
                .replace(' ', '_');

        try {
            return Categoria.valueOf(normalizada);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Categoria invalida");
        }
    }

    private Prioridade parsePrioridade(String prioridadeTexto) {
        if (prioridadeTexto == null) {
            throw new IllegalArgumentException("Prioridade invalida");
        }

        String normalizada = prioridadeTexto.trim().toUpperCase();
        if (normalizada.isEmpty()) {
            throw new IllegalArgumentException("Prioridade invalida");
        }

        try {
            return Prioridade.valueOf(normalizada);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Prioridade invalida");
        }
    }
}