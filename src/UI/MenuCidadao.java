package UI;

import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import java.util.List;
import Models.Solicitacao;

import java.time.format.DateTimeFormatter;
import java.util.Scanner;

public class MenuCidadao {
    private static final DateTimeFormatter FORMATADOR_DATA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private final Scanner scanner;
    private final SolicitacaoService solicitacaoService;

    public MenuCidadao(Scanner scanner, SolicitacaoRepositoryInterface repo) {
        this.scanner = scanner;
        this.solicitacaoService = new SolicitacaoService(repo);
    }

    public void exibir() {
        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("\n-----------Menu do Cidadão-----------");
            System.out.println("1 - Nova solicitação");
            System.out.println("2 - Ver solicitações");
            System.out.println("3 - Consultar por protocolo");
            System.out.println("0 - Voltar");
            System.out.println("-------------------------------------");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    novaSolicitacao();
                    break;
                case "2":
                    verSolicitacoes();
                    break;
                case "3":
                    consultarPorProtocolo();
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

    private void novaSolicitacao() {
        System.out.println("Deseja se identificar? (S/N)");
        String resposta = scanner.nextLine().trim();

        String nome = null;
        String contato = null;

        if (resposta.equalsIgnoreCase("S")) {
            System.out.print("Nome: ");
            nome = scanner.nextLine();
            System.out.print("Contato: ");
            contato = scanner.nextLine();
        } else if (!resposta.equalsIgnoreCase("N")) {
            System.out.println("Resposta inválida. Use S ou N.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        System.out.println("Categoria (ILUMINACAO, BURACO, LIMPEZA, SAUDE, OUTRO): ");
        String categoria = scanner.nextLine();
        System.out.print("Bairro: ");
        String bairro = scanner.nextLine();
        System.out.print("Título: ");
        String titulo = scanner.nextLine();
        System.out.print("Descrição: ");
        String descricao = scanner.nextLine();
        System.out.println("Prioridade (BAIXA, MEDIA, ALTA, URGENTE): ");
        String prioridade = scanner.nextLine();

        try {
            String protocolo = solicitacaoService.criarSolicitacao(
                    nome, contato, categoria, titulo, descricao, bairro, prioridade
            );
            System.out.println("Solicitação criada com sucesso! Protocolo: " + protocolo);
        } catch (IllegalArgumentException e) {
            System.out.println("Categoria ou prioridade inválida. Confira os valores digitados.");
        }
        ConsoleUtils.pausar(scanner);
    }

    private void verSolicitacoes() {
        List<Solicitacao> lista = solicitacaoService.listarSolicitacoes();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            ConsoleUtils.pausar(scanner);
            return;
        }
        lista.forEach(s -> System.out.println(exibirLinhaDashboard(s)));
        ConsoleUtils.pausar(scanner);
    }

    private void consultarPorProtocolo() {
        System.out.print("Informe o protocolo: ");
        String protocolo = scanner.nextLine().trim();

        Solicitacao solicitacao = solicitacaoService.buscarPorProtocolo(protocolo);
        if (solicitacao == null) {
            System.out.println("Solicitação não encontrada para o protocolo informado.");
            ConsoleUtils.pausar(scanner);
            return;
        }

        exibirDetalhesSolicitacao(solicitacao);
        System.out.println("-------------------------------------");
        ConsoleUtils.pausar(scanner);
    }

    private void exibirDetalhesSolicitacao(Solicitacao solicitacao) {
        System.out.println("-------------------------------------");
        System.out.println("Protocolo : " + solicitacao.getProtocolo());
        System.out.println("Título    : " + solicitacao.getTitle());
        System.out.println("Status    : " + solicitacao.getStatus());
        System.out.println("Categoria : " + solicitacao.getCategoria());
        System.out.println("Bairro    : " + solicitacao.getLocalizacao());
        System.out.println("Descrição : " + solicitacao.getDescricao());
        System.out.println("Prioridade: " + solicitacao.getPrioridade());
        System.out.println("Prazo SLA : " + solicitacao.getPrazoAlvo().format(FORMATADOR_DATA));
    }

    private String exibirLinhaDashboard(Solicitacao solicitacao) {
        return "[" + solicitacao.getPrioridade() + "] " + solicitacao.getProtocolo() +
                " | Status: " + solicitacao.getStatus() +
                " | Prazo: " + solicitacao.getPrazoAlvo().format(FORMATADOR_DATA) +
                " | Bairro: " + solicitacao.getLocalizacao();
    }
}