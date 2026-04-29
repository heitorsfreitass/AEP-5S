package com.aep5s.repositories;

import com.aep5s.enums.*;
import com.aep5s.models.Solicitacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SolicitacaoRepository extends JpaRepository<Solicitacao, Long> {

    Solicitacao findByProtocolo(String protocolo);

    List<Solicitacao> findByStatus(StatusSolicitacao status);

    List<Solicitacao> findByCategoria(Categoria categoria);

    List<Solicitacao> findByLocalizacaoIgnoreCase(String localizacao);

    List<Solicitacao> findByPrioridade(Prioridade prioridade);
}
