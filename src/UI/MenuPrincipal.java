package UI;

import Repositories.SolicitacaoRepository;
import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import Storage.SolicitacoesSeeder;

import java.util.Scanner;

public class MenuPrincipal {
    private final Scanner scanner = new Scanner(System.in);
    // repository criado UMA vez aqui e compartilhado com todos os menus
    private final SolicitacaoRepositoryInterface repo = new SolicitacaoRepository();
    private final SolicitacaoService solicitacaoService = new SolicitacaoService(repo);
    // Arquivo TXT único usado para manter dados entre execuções.
    private static final String ARQUIVO_DADOS = "solicitacoes.txt";

    public MenuPrincipal() {
        try {
            SolicitacoesSeeder seeder = new SolicitacoesSeeder(solicitacaoService);
            SolicitacoesSeeder.ResultadoSeeder resultado = seeder.inicializar(ARQUIVO_DADOS);
            if (resultado.isSeedCriada()) {
                System.out.println("Dados iniciais criados: " + resultado.getQuantidade() + " solicitacoes");
            } else {
                System.out.println("Solicitacoes carregadas: " + resultado.getQuantidade());
            }
        } catch (IllegalStateException e) {
            System.out.println("Nao foi possivel carregar os dados salvos.");
        }
    }


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
                    System.out.println("Encerrando o sistema...");
                    try {
                        // Persistência centralizada no service para manter separação de camadas.
                        solicitacaoService.salvarSolicitacoes(ARQUIVO_DADOS);
                    } catch (IllegalStateException e) {
                        System.out.println("Nao foi possivel salvar os dados.");
                        ConsoleUtils.pausar(scanner);
                    }
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
                    ConsoleUtils.pausar(scanner);
            }
        }
    }
}