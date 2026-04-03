package UI;

import Repositories.SolicitacaoRepository;
import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import Models.Solicitacao;

import java.util.List;
import java.util.Scanner;

public class MenuServidor {

    private final Scanner scanner;
    // agora passa o repository da interface no construtor da service
    private final SolicitacaoRepositoryInterface repo = new SolicitacaoRepository();
    private final SolicitacaoService solicitacaoService = new SolicitacaoService(repo);

    //criei esse construtor para não precisar criar um novo scanner em cada menu, evitar bugs de leitura,
    //quando um metodo do UI for chamado em outro, o scanner é passado como parâmetro.
    public MenuServidor(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("-----------Menu do Servidor-----------");
            System.out.println("1 - Ver todas as solicitações");
            System.out.println("2 - Atualizar status de solicitação");
            System.out.println("0 - Voltar");
            System.out.println("--------------------------------------");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    listarTodas();
                    break;
                case "2":
                    atualizarStatus();
                    break;
                case "0":
                    System.out.println("Voltando ao menu principal...");
                    ConsoleUtils.pausar(scanner);
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    ConsoleUtils.pausar(scanner);
            }
        }
    }

    private void listarTodas() {
        List<Solicitacao> lista = solicitacaoService.listarSolicitacoes();
        if (lista.isEmpty()) {
            System.out.println("Nenhuma solicitação encontrada.");
            ConsoleUtils.pausar(scanner);
            return;
        }
        lista.forEach(s -> System.out.println(
                "[" + s.getPrioridade() + "] " + s.getProtocolo() +
                        " | " + s.getCategoria() +
                        " | " + s.getStatus() +
                        " | " + s.getLocalizacao()
        ));
        ConsoleUtils.pausar(scanner);
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
        System.out.println("Novo status (TRIAGEM, EM_EXECUCAO, RESOLVIDO, ENCERRADO): ");
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
}