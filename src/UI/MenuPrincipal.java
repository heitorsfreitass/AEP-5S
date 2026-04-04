package UI;

import Repositories.SolicitacaoRepository;
import Repositories.SolicitacaoRepositoryInterface;
import java.util.Scanner;

public class MenuPrincipal {
    private final Scanner scanner = new Scanner(System.in);
    // repository criado UMA vez aqui e compartilhado com todos os menus
    private final SolicitacaoRepositoryInterface repo = new SolicitacaoRepository();

    public void exibir() {
        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("\n========== MENU ==========");
            System.out.println("1 - Painel cidadão");
            System.out.println("-------------------------------------");
            System.out.println("2 - Painel servidor");
            System.out.println("-------------------------------------");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    new MenuCidadao(scanner, repo).exibir();
                    break;
                case "2":
                    new MenuServidor(scanner, repo).exibir();
                    break;
                case "0":
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    ConsoleUtils.pausar(scanner);
            }
        }
    }
}