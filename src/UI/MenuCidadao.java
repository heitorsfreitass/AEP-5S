package UI;

import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import java.util.List;
import java.util.ArrayList;
import Models.Solicitacao;
import Enums.Prioridade;
import Enums.Categoria;

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

        System.out.println("\n---------- FILTROS ----------");
        System.out.println("Deseja filtrar? (S/N): ");
        String desejafiltrar = scanner.nextLine().trim();

        if (desejafiltrar.equalsIgnoreCase("S")) {
            aplicarFiltros(lista);
        }

        ConsoleUtils.pausar(scanner);
    }

    private void aplicarFiltros(List<Solicitacao> lista) {
        List<Solicitacao> listaFiltrada = new ArrayList<>(lista);

        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("\n---------- LISTA ATUAL (" + listaFiltrada.size() + ") ----------");
            if (listaFiltrada.isEmpty()) {
                System.out.println("Nenhuma solicitação com os filtros atuais.");
            } else {
                listaFiltrada.forEach(s -> System.out.println(exibirLinhaDashboard(s)));
            }

            System.out.println("\n---------- OPÇÕES DE FILTRO ----------");
            System.out.println("1 - Filtrar por prioridade");
            System.out.println("2 - Filtrar por bairro");
            System.out.println("3 - Filtrar por categoria");
            System.out.println("4 - Limpar filtros");
            System.out.println("0 - Voltar");
            System.out.print("Escolha: ");

            String opcao = scanner.nextLine().trim();

            switch (opcao) {
                case "1":
                    listaFiltrada = filtrarPorPrioridade(listaFiltrada);
                    break;
                case "2":
                    listaFiltrada = filtrarPorBairro(listaFiltrada);
                    break;
                case "3":
                    listaFiltrada = filtrarPorCategoria(listaFiltrada);
                    break;
                case "4":
                    listaFiltrada = new ArrayList<>(lista);
                    System.out.println("Filtros removidos. Exibindo todas as solicitações novamente.");
                    ConsoleUtils.pausar(scanner);
                    break;
                case "0":
                    return;
                default:
                    System.out.println("Opção inválida.");
                    ConsoleUtils.pausar(scanner);
            }
        }
    }

    private List<Solicitacao> filtrarPorPrioridade(List<Solicitacao> lista) {
        System.out.println("\nPrioridades disponíveis:");
        System.out.println("1 - BAIXA");
        System.out.println("2 - MEDIA");
        System.out.println("3 - ALTA");
        System.out.println("4 - URGENTE");
        System.out.print("Escolha (INFORME O NÚMERO): ");
        
        String escolha = scanner.nextLine().trim();
        Prioridade prioridade = null;
        
        switch (escolha) {
            case "1":
                prioridade = Prioridade.BAIXA;
                break;
            case "2":
                prioridade = Prioridade.MEDIA;
                break;
            case "3":
                prioridade = Prioridade.ALTA;
                break;
            case "4":
                prioridade = Prioridade.URGENTE;
                break;
            default:
                System.out.println("Opção inválida.");
                return lista;
        }
        
        return solicitacaoService.filtrarPorPrioridade(prioridade);
    }

    private List<Solicitacao> filtrarPorBairro(List<Solicitacao> lista) {
        System.out.print("Informe o bairro: ");
        String bairro = scanner.nextLine().trim();
        
        return solicitacaoService.filtrarPorBairro(bairro);
    }

    private List<Solicitacao> filtrarPorCategoria(List<Solicitacao> lista) {
        System.out.println("\nCategorias disponíveis:");
        System.out.println("1 - ILUMINACAO");
        System.out.println("2 - BURACO");
        System.out.println("3 - LIMPEZA");
        System.out.println("4 - SAUDE");
        System.out.println("5 - OUTRO");
        System.out.print("Escolha (INFORME O NÚMERO): ");
        
        String escolha = scanner.nextLine().trim();
        Categoria categoria = null;
        
        switch (escolha) {
            case "1":
                categoria = Categoria.ILUMINACAO;
                break;
            case "2":
                categoria = Categoria.BURACO;
                break;
            case "3":
                categoria = Categoria.LIMPEZA;
                break;
            case "4":
                categoria = Categoria.SAUDE;
                break;
            case "5":
                categoria = Categoria.OUTRO;
                break;
            default:
                System.out.println("Opção inválida.");
                return lista;
        }
        
        return solicitacaoService.filtrarPorCategoria(categoria);
    }

    private void exibirListaFiltrada(List<Solicitacao> lista) {
        if (lista.isEmpty()) {
            System.out.println("\nNenhuma solicitação encontrada com os filtros aplicados.");
            return;
        }

        System.out.println("\n---------- RESULTADOS (" + lista.size() + ") ----------");
        lista.forEach(s -> System.out.println(exibirLinhaDashboard(s)));
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

        System.out.println("Histórico  :");
        if (solicitacao.getHistorico().isEmpty()) {
            System.out.println("- Sem movimentações até o momento.");
        } else {
            solicitacao.getHistorico().forEach(item -> System.out.println("- " + item));
        }

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
                " | Categoria: " + solicitacao.getCategoria() +
                " | Status: " + solicitacao.getStatus() +
                " | Prazo: " + solicitacao.getPrazoAlvo().format(FORMATADOR_DATA) +
                " | Bairro: " + solicitacao.getLocalizacao();
    }
}