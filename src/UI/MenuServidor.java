package UI;

import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import Models.Solicitacao;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class MenuServidor {
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Scanner scanner;
    private final SolicitacaoService solicitacaoService;

    public MenuServidor(Scanner scanner, SolicitacaoRepositoryInterface repo) {
        this.scanner = scanner;
        this.solicitacaoService = new SolicitacaoService(repo);
    }

    public void exibir() {
        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("-----------Menu do Servidor-----------");
            System.out.println("1 - Atualizar status de solicitação");
            System.out.println("2 - Consultar atendimento específico");
            System.out.println("3 - Ver fila de atendimento (SLA)");
            System.out.println("0 - Voltar");
            System.out.println("--------------------------------------");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    atualizarStatus();
                    break;
                case "2":
                    consultarAtendimentoEspecifico();
                    break;
                case "3":
                    listarFilaSla();
                    break;
                case "0":
                    System.out.println("Voltando ao menu principal...");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    ConsoleUtils.pausar(scanner);
            }
        }
    }

    private void atualizarStatus() {
        System.out.print("Protocolo da solicitação: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = solicitacaoService.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("Solicitação não encontrada.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        System.out.println("Status atual: " + solicitacao.getStatus());
        System.out.print("Novo status (TRIAGEM, EM_EXECUCAO, RESOLVIDO, ENCERRADO): ");
        String novoStatus = scanner.nextLine().trim();

        System.out.print("Comentário (obrigatório): ");
        String comentario = scanner.nextLine().trim();
        if (comentario.isEmpty()) {
            System.out.println("Comentário é obrigatório.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        System.out.print("Seu nome (responsável): ");
        String responsavel = scanner.nextLine().trim();

        try {
            solicitacaoService.atualizarStatus(protocolo, novoStatus, comentario, responsavel);
            System.out.println("Status atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            System.out.println("Status inválido. Confira os valores digitados.");
        }
        ConsoleUtils.pausar(scanner);
    }

    private void listarFilaSla() {
        List<Solicitacao> fila = solicitacaoService.listarFilaAtendimentoSla();

        if (fila.isEmpty()) {
            System.out.println("Nenhuma solicitação ativa na fila de SLA.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        fila.forEach(s -> System.out.println(
                "[" + s.getPrioridade() + "] " + s.getProtocolo() +
                        " | Status: " + s.getStatus() +
                        " | Prazo: " + s.getPrazoAlvo().format(FORMATADOR_DATA) +
                        " | Bairro: " + s.getLocalizacao()
        ));

        ConsoleUtils.pausar(scanner);
    }

    private void consultarAtendimentoEspecifico() {
        System.out.print("Informe o protocolo da solicitação: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = solicitacaoService.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("Solicitação não encontrada.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        exibirDetalhesSolicitacao(solicitacao);
        ConsoleUtils.pausar(scanner);
    }

    private void exibirDetalhesSolicitacao(Solicitacao solicitacao) {
        System.out.println("--------------------------------------");
        System.out.println("Protocolo : " + solicitacao.getProtocolo());
        System.out.println("Título    : " + solicitacao.getTitle());
        System.out.println("Descrição : " + solicitacao.getDescricao());
        System.out.println("Categoria : " + solicitacao.getCategoria());
        System.out.println("Prioridade: " + solicitacao.getPrioridade());
        System.out.println("Status    : " + solicitacao.getStatus());
        System.out.println("Bairro    : " + solicitacao.getLocalizacao());
        System.out.println("Aberto em : " + solicitacao.getDataAbertura().format(FORMATADOR_DATA));
        System.out.println("Prazo SLA : " + solicitacao.getPrazoAlvo().format(FORMATADOR_DATA));

        String nomeSolicitante = solicitacao.getSolicitante() == null ? "Não informado" : solicitacao.getSolicitante().getNome();
        String contatoSolicitante = (solicitacao.getSolicitante() == null || solicitacao.getSolicitante().getContato() == null
                || solicitacao.getSolicitante().getContato().trim().isEmpty())
                ? "Não informado"
                : solicitacao.getSolicitante().getContato();

        System.out.println("Solicitante: " + nomeSolicitante);
        System.out.println("Contato    : " + contatoSolicitante);

        System.out.println("Histórico  :");
        if (solicitacao.getHistorico().isEmpty()) {
            System.out.println("- Sem movimentações até o momento.");
        } else {
            solicitacao.getHistorico().forEach(item -> System.out.println("- " + item));
        }
        System.out.println("--------------------------------------");
    }
}