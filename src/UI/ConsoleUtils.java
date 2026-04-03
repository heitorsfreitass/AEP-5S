package UI;

import java.util.Scanner;

public final class ConsoleUtils {

    private ConsoleUtils() {
        // utilitário estático
    }

    public static void limparTela() {
        System.out.print("\u001b[H\u001b[2J");
        System.out.flush();

        for (int i = 0; i < 120; i++) {
            System.out.println();
        }
    }

    public static void pausar(Scanner scanner) {
        System.out.print("\nPressione Enter para continuar...");
        scanner.nextLine();
        limparTela();
    }
}

