package UI;

import java.util.Scanner;

public class MenuPrincipal {

    // mudei pra final ja que agora com os construtores esse scanner aqui nunca vai mudar
    private final Scanner scanner = new Scanner(System.in);

    public void exibir() {

        while (true) {
            System.out.println("\n========== MENU ==========");
            System.out.println("1 - Painel cidadão");
            System.out.println("-------------------------------------");
            System.out.println("2 - Painel servidor");
            System.out.println("-------------------------------------");
            System.out.println("0 - Sair");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine();

            // switch verifica o que o usuário digitou e chama o menu certo
            switch (opcao) {
                case "1":
                    new MenuCidadao(scanner).exibir();
                    break;
                case "2":
                    new MenuServidor(scanner).exibir();
                    break;
                case "0":
                    System.out.println("Encerrando o sistema. Até logo!");
                    return;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }
}