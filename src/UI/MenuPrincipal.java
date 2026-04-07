package UI;

import Repositories.SolicitacaoRepository;
import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;

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
            int carregadas = solicitacaoService.carregarSolicitacoes(ARQUIVO_DADOS);
            if (carregadas == 0) {
                // Seed inicial: só executa quando o arquivo ainda não tem registros.
                popularDadosIniciais();
                solicitacaoService.salvarSolicitacoes(ARQUIVO_DADOS);
                System.out.println("Dados iniciais criados: 10 solicitacoes");
            } else {
                System.out.println("Solicitacoes carregadas: " + carregadas);
            }
        } catch (IllegalStateException e) {
            System.out.println("Nao foi possivel carregar os dados salvos.");
        }
    }

    private void popularDadosIniciais() {
        // Dados base para facilitar validação manual do fluxo na primeira execução.
        String p1 = solicitacaoService.criarSolicitacao(
                "Ana Silva", "(11) 99999-1111", "ILUMINACAO",
                "Poste apagado", "Poste sem luz ha 3 noites na rua principal.",
                "ZONA01", "ALTA"
        );

        String p2 = solicitacaoService.criarSolicitacao(
                "", "", "BURACO",
                "Buraco grande", "Buraco profundo em frente a escola municipal.",
                "ZONA02", "URGENTE"
        );

        String p3 = solicitacaoService.criarSolicitacao(
                "Carlos Mendes", "(11) 98888-2222", "LIMPEZA",
                "Lixo acumulado", "Acumulo de lixo na calcada desde domingo.",
                "ZONA03", "MEDIA"
        );

        String p4 = solicitacaoService.criarSolicitacao(
                "Mariana Costa", "(11) 97777-3333", "SAUDE",
                "Fila extensa no posto", "Demora excessiva no atendimento da unidade de saude.",
                "ZONA01", "ALTA"
        );

        String p5 = solicitacaoService.criarSolicitacao(
                "", "", "ASSEDIO",
                "Assedio em espaco publico", "Relato anonimo de assedio recorrente em ponto de onibus.",
                "ZONA04", "BAIXA"
        );

        String p6 = solicitacaoService.criarSolicitacao(
                "Roberta Lima", "(11) 96666-4444", "ILUMINACAO",
                "Lampada piscando", "Lampada publica piscando intermitente em via de acesso.",
                "ZONA02", "BAIXA"
        );

        String p7 = solicitacaoService.criarSolicitacao(
                "Joao Pereira", "(11) 95555-1212", "BURACO",
                "Asfalto cedendo", "Trecho com asfalto cedendo perto da faixa de pedestres.",
                "ZONA05", "ALTA"
        );

        String p8 = solicitacaoService.criarSolicitacao(
                "", "", "LIMPEZA",
                "Entulho na praca", "Entulho acumulado ao lado do parquinho infantil.",
                "ZONA03", "MEDIA"
        );

        String p9 = solicitacaoService.criarSolicitacao(
                "Patricia Souza", "(11) 94444-5656", "INJURIA",
                "Injuria em atendimento", "Registro de injuria verbal durante atendimento presencial.",
                "ZONA06", "URGENTE"
        );

        String p10 = solicitacaoService.criarSolicitacao(
                "Rafael Gomes", "(11) 93333-7878", "CRIME",
                "Suspeita de crime", "Comunicacao de atividade criminosa recorrente na regiao.",
                "ZONA04", "BAIXA"
        );

        solicitacaoService.atualizarStatus(p1, "TRIAGEM", "Encaminhado para equipe tecnica.", "Servidor A");
        solicitacaoService.atualizarStatus(p2, "TRIAGEM", "Demanda prioritaria em avaliacao.", "Servidor B");
        solicitacaoService.atualizarStatus(p2, "EM_EXECUCAO", "Equipe deslocada para reparo emergencial.", "Servidor B");
        solicitacaoService.atualizarStatus(p3, "TRIAGEM", "Aguardando roteiro de limpeza urbana.", "Servidor C");
        solicitacaoService.atualizarStatus(p4, "TRIAGEM", "Encaminhado para coordenacao da unidade.", "Servidor D");
        solicitacaoService.atualizarStatus(p5, "TRIAGEM", "Denuncia registrada para analise preliminar.", "Servidor E");
        solicitacaoService.atualizarStatus(p6, "TRIAGEM", "Solicitacao recebida e validada.", "Servidor F");
        solicitacaoService.atualizarStatus(p7, "TRIAGEM", "Equipe de obras notificada para vistoria.", "Servidor G");
        solicitacaoService.atualizarStatus(p8, "TRIAGEM", "Demanda incluida na rota de limpeza.", "Servidor H");
        solicitacaoService.atualizarStatus(p9, "TRIAGEM", "Encaminhado para farmacia da unidade.", "Servidor I");
        solicitacaoService.atualizarStatus(p9, "EM_EXECUCAO", "Reposicao de estoque em andamento.", "Servidor I");
        solicitacaoService.atualizarStatus(p10, "TRIAGEM", "Queixa registrada para avaliacao da fiscalizacao.", "Servidor J");
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