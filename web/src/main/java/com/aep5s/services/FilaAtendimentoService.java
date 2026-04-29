package com.aep5s.services;

import com.aep5s.enums.Prioridade;
import com.aep5s.enums.StatusSolicitacao;
import com.aep5s.models.Solicitacao;
import com.aep5s.repositories.SolicitacaoRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class FilaAtendimentoService {
    private static final EnumSet<StatusSolicitacao> STATUS_ATIVOS = EnumSet.of(
            StatusSolicitacao.ABERTO,
            StatusSolicitacao.TRIAGEM,
            StatusSolicitacao.EM_EXECUCAO
    );

    private final SolicitacaoRepository solicitacaoRepository;

    public FilaAtendimentoService(SolicitacaoRepository solicitacaoRepository) {
        this.solicitacaoRepository = solicitacaoRepository;
    }

    public List<Solicitacao> listarOrdenadoPorPrioridade() {
        return solicitacaoRepository.findAll()
                .stream()
                .sorted((a, b) -> Integer.compare(
                        pesoPrioridade(b.getPrioridade()),
                        pesoPrioridade(a.getPrioridade())
                ))
                .collect(Collectors.toList());
    }

    public List<Solicitacao> listarPorStatus(StatusSolicitacao status) {
        return solicitacaoRepository.findAll()
                .stream()
                .filter(s -> s.getStatus() == status)
                .collect(Collectors.toList());
    }

    public List<Solicitacao> listarFilaPorSla() {
        return solicitacaoRepository.findAll()
                .stream()
                .filter(s -> STATUS_ATIVOS.contains(s.getStatus()))
                .sorted((a, b) -> {
                    int prazo = a.getPrazoAlvo().compareTo(b.getPrazoAlvo());
                    if (prazo != 0) return prazo;

                    return Integer.compare(
                            pesoPrioridade(b.getPrioridade()),
                            pesoPrioridade(a.getPrioridade())
                    );
                })
                .collect(Collectors.toList());
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

