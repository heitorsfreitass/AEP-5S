package UI;

import Services.SolicitacaoService;

import java.util.Scanner;

public class MenuCidadao {
    private final Scanner scanner;
    private final SolicitacaoService solicitacaoService = new SolicitacaoService();

    //criei esse construtor para não precisar criar um novo scanner em cada menu, evitar bugs de leitura,
    //quando um metodo do UI for chamado em outro, o scanner é passado como parâmetro.
    public MenuCidadao(Scanner scanner) {
        this.scanner = scanner;
    }

    public void exibir() {
        while (true) {
            System.out.println("\n-----------Menu do Cidadão-----------");
            System.out.println("1 - Nova solicitação");
            System.out.println("2 - Ver solicitações");
            System.out.println("0 - Voltar");
            System.out.println("-------------------------------------");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    System.out.println("Deseja se identificar? (S/N)");
                    String resposta = scanner.nextLine().trim();
                    
                    //Começa como null aqui, porque se for "não", não precisarmos preencher, e se for "sim", vai preencher com o que o usuário digitar
                    String solicitacaoNome = null;
                    String solicitacaoContato = null;

                    if (resposta.equalsIgnoreCase("S")) {
                        System.out.println("Digite seu nome:");
                        solicitacaoNome = scanner.nextLine();
                        System.out.println("Contato: ");
                        solicitacaoContato = scanner.nextLine();
                    } else if (!resposta.equalsIgnoreCase("N")) {
                        System.out.println("Resposta inválida. Use S ou N.");
                        break;
                    }

                    //1. Pergunta a categoria
                    System.out.println("Categoria (ILUMINACAO, BURACO, LIMPEZA, SAUDE, OUTRO): ");
                    String solicitacaoCategoria = scanner.nextLine();

                    //2. Localização
                    System.out.println("Bairro: ");
                    String solicitacaoBairro = scanner.nextLine();

                    //3. Título e descrição
                    System.out.println("Título da Solicitação: ");
                    String solicitacaoTitle = scanner.nextLine();
                    System.out.println("Descrição da Solicitação: ");
                    String solicitacaoDescription = scanner.nextLine();

                    //4. Prioridade
                    System.out.println("Prioridade (BAIXA, MEDIA, ALTA, URGENTE): ");
                    String solicitacaoPrioridade = scanner.nextLine();


                    try {
                        solicitacaoService.criarSolicitacao(
                                solicitacaoNome,
                                solicitacaoContato,
                                solicitacaoCategoria,
                                solicitacaoTitle,
                                solicitacaoDescription,
                                solicitacaoBairro,
                                solicitacaoPrioridade
                        );
                    } catch (IllegalArgumentException e) {
                        System.out.println("Categoria ou prioridade inválida. Confira os valores digitados.");
                    }
                    break;
                case "2":
                    System.out.println("-----------Solicitações-----------");
                    solicitacaoService.listarSolicitacoes();
                    break;
                case "0":
                    System.out.println("Voltando ao menu principal...");
                    return; //já volta para o menu principal, que esta executando em loop
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }


    }
}
