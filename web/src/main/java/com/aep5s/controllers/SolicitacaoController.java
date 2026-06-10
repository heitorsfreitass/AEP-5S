package com.aep5s.controllers;

import com.aep5s.enums.Categoria;
import com.aep5s.enums.Prioridade;
import com.aep5s.enums.StatusSolicitacao;
import com.aep5s.models.Solicitacao;
import com.aep5s.services.SolicitacaoService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class SolicitacaoController {

    private final SolicitacaoService solicitacaoService;

    public SolicitacaoController(SolicitacaoService solicitacaoService) {
        this.solicitacaoService = solicitacaoService;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        List<Solicitacao> todas = solicitacaoService.listarSolicitacoes();
        long abertas   = todas.stream().filter(s -> s.getStatus() == StatusSolicitacao.ABERTO).count();
        long emExec    = todas.stream().filter(s -> s.getStatus() == StatusSolicitacao.EM_EXECUCAO).count();
        long resolvidas = todas.stream().filter(s -> s.getStatus() == StatusSolicitacao.RESOLVIDO
                || s.getStatus() == StatusSolicitacao.ENCERRADO).count();

        model.addAttribute("total",     todas.size());
        model.addAttribute("abertas",   abertas);
        model.addAttribute("emExec",    emExec);
        model.addAttribute("resolvidas", resolvidas);
        return "dashboard";
    }

    @GetMapping("/solicitacao")
    public String formSolicitacao(Model model) {
        model.addAttribute("categorias", Categoria.values());
        model.addAttribute("prioridades", Prioridade.values());
        return "solicitacao";
    }

    @PostMapping("/solicitacao")
    public String enviarSolicitacao(
            @RequestParam(required = false) String tipoSolicitante,
            @RequestParam(required = false) String nome,
            @RequestParam(required = false) String contato,
            @RequestParam String categoria,
            @RequestParam String prioridade,
            @RequestParam String titulo,
            @RequestParam String descricao,
            @RequestParam(required = false) String localizacao,
            RedirectAttributes redirectAttributes) {

        boolean anonimo = "anonimo".equals(tipoSolicitante);
        String nomeReal    = anonimo ? null : nome;
        String contatoReal = anonimo ? null : contato;

        String protocolo = solicitacaoService.criarSolicitacao(
                nomeReal, contatoReal, categoria, titulo, descricao,
                localizacao == null ? "" : localizacao, prioridade);

        redirectAttributes.addFlashAttribute("protocoloGerado", protocolo);
        return "redirect:/solicitacao/sucesso";
    }

    @GetMapping("/solicitacao/sucesso")
    public String sucesso() {
        return "sucesso";
    }

    @GetMapping("/protocolo")
    public String consultaProtocolo(@RequestParam(required = false) String protocolo, Model model) {
        if (protocolo != null && !protocolo.isBlank()) {
            Solicitacao sol = solicitacaoService.buscarPorProtocolo(protocolo.trim());
            if (sol != null) {
                model.addAttribute("solicitacao", sol);
                model.addAttribute("agora", LocalDateTime.now());
            } else {
                model.addAttribute("erro", "Protocolo não encontrado.");
            }
        }
        return "protocolo";
    }

    @GetMapping("/servidor")
    public String painelServidor(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String prioridade,
            @RequestParam(required = false) String categoria,
            @RequestParam(required = false) String bairro,
            Model model) {

        List<Solicitacao> lista;

        if (categoria != null && !categoria.isBlank()) {
            lista = solicitacaoService.filtrarPorCategoria(Categoria.valueOf(categoria));
        } else if (prioridade != null && !prioridade.isBlank()) {
            lista = solicitacaoService.filtrarPorPrioridade(Prioridade.valueOf(prioridade));
        } else if (bairro != null && !bairro.isBlank()) {
            lista = solicitacaoService.filtrarPorBairro(bairro);
        } else {
            lista = solicitacaoService.listarSolicitacoes();
        }

        if (status != null && !status.isBlank()) {
            StatusSolicitacao statusEnum = StatusSolicitacao.valueOf(status);
            lista = lista.stream().filter(s -> s.getStatus() == statusEnum).toList();
        }

        model.addAttribute("solicitacoes", lista);
        model.addAttribute("categorias",   Categoria.values());
        model.addAttribute("prioridades",  Prioridade.values());
        model.addAttribute("statusList",   StatusSolicitacao.values());
        model.addAttribute("agora",        LocalDateTime.now());
        return "servidor";
    }

    @PostMapping("/servidor/atualizar")
    public String atualizarStatus(
            @RequestParam String protocolo,
            @RequestParam String novoStatus,
            @RequestParam String comentario,
            @RequestParam String responsavel,
            RedirectAttributes redirectAttributes) {

        try {
            solicitacaoService.atualizarStatus(protocolo, novoStatus, comentario, responsavel);
            redirectAttributes.addFlashAttribute("sucesso", "Status atualizado com sucesso.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("erro", e.getMessage());
        }
        return "redirect:/servidor";
    }

    @GetMapping("/sla")
    public String filaSla(Model model) {
        LocalDateTime agora = LocalDateTime.now();
        List<Solicitacao> fila = solicitacaoService.listarFilaAtendimentoSla();

        Map<String, String> tempoRestante = new LinkedHashMap<>();
        for (Solicitacao s : fila) {
            Duration dur = Duration.between(agora, s.getPrazoAlvo());
            long absMin = Math.abs(dur.toMinutes());
            long h = absMin / 60;
            long m = absMin % 60;
            String texto = (dur.isNegative() ? "-" : "") + h + "h " + m + "m";
            tempoRestante.put(s.getProtocolo(), texto);
        }

        model.addAttribute("fila", fila);
        model.addAttribute("tempoRestante", tempoRestante);
        model.addAttribute("agora", agora);
        return "sla";
    }
}
