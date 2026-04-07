package UI;

import Repositories.SolicitacaoRepositoryInterface;
import Services.SolicitacaoService;
import Models.Solicitacao;
import Enums.Prioridade;
import Enums.Categoria;

import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;
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

        System.out.println("\n---------- FILTROS ----------");
        System.out.println("Deseja filtrar? (S/N): ");
        String desejafiltrar = scanner.nextLine().trim();
        
        if (desejafiltrar.equalsIgnoreCase("S")) {
            aplicarFiltros(fila);
        }
        
         ConsoleUtils.pausar(scanner);
    }

    private void aplicarFiltros(List<Solicitacao> fila) {
        List<Solicitacao> listaFiltrada = new ArrayList<>(fila);

        while (true) {
            ConsoleUtils.limparTela();
            System.out.println("\n---------- FILA ATUAL (" + listaFiltrada.size() + ") ----------");
            if (listaFiltrada.isEmpty()) {
                System.out.println("Nenhuma solicitação com os filtros atuais.");
            } else {
                listaFiltrada.forEach(s -> System.out.println(
                        "[" + s.getPrioridade() + "] " + s.getProtocolo() +
                                " | Status: " + s.getStatus() +
                                " | Prazo: " + s.getPrazoAlvo().format(FORMATADOR_DATA) +
                                " | Bairro: " + s.getLocalizacao()
                ));
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
                    listaFiltrada = new ArrayList<>(fila);
                    System.out.println("Filtros removidos. Exibindo fila completa novamente.");
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
        lista.forEach(s -> System.out.println(
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