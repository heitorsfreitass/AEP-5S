package Storage;

import Enums.Categoria;
import Enums.Prioridade;
import Enums.StatusSolicitacao;
import Models.HistoricoStatus;
import Models.Solicitacao;
import Models.Usuario;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class SolicitacaoTxtStorage {

    public void salvar(String nomeArquivo, List<Solicitacao> solicitacoes) {
        Path caminho = Paths.get(nomeArquivo);
        try {
            Path parent = caminho.getParent();
            if (parent != null && !Files.exists(parent)) {
                Files.createDirectories(parent);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(caminho, StandardCharsets.UTF_8)) {
                // Formato: chave=valor por linha e separador '---' entre solicitações.
                for (Solicitacao s : solicitacoes) {
                    escreverLinha(writer, "protocolo", s.getProtocolo());
                    escreverLinha(writer, "title", s.getTitle());
                    escreverLinha(writer, "descricao", s.getDescricao());
                    escreverLinha(writer, "localizacao", s.getLocalizacao());
                    escreverLinha(writer, "dataAbertura", s.getDataAbertura() == null ? null : s.getDataAbertura().toString());
                    escreverLinha(writer, "prazoAlvo", s.getPrazoAlvo() == null ? null : s.getPrazoAlvo().toString());
                    escreverLinha(writer, "categoria", s.getCategoria() == null ? null : s.getCategoria().name());
                    escreverLinha(writer, "prioridade", s.getPrioridade() == null ? null : s.getPrioridade().name());
                    escreverLinha(writer, "status", s.getStatus() == null ? null : s.getStatus().name());

                    Usuario u = s.getSolicitante();
                    escreverLinha(writer, "solicitanteNome", u == null ? null : u.getNome());
                    escreverLinha(writer, "solicitanteContato", u == null ? null : u.getContato());

                    List<HistoricoStatus> historico = s.getHistorico();
                    escreverLinha(writer, "historicoCount", String.valueOf(historico.size()));
                    for (int i = 0; i < historico.size(); i++) {
                        HistoricoStatus h = historico.get(i);
                        escreverLinha(writer, "h." + i + ".statusAnterior", h.getStatusAnterior().name());
                        escreverLinha(writer, "h." + i + ".statusNovo", h.getStatusNovo().name());
                        escreverLinha(writer, "h." + i + ".comentario", h.getComentario());
                        escreverLinha(writer, "h." + i + ".responsavel", h.getResponsavel());
                        escreverLinha(writer, "h." + i + ".dataHora", h.getDataHora() == null ? null : h.getDataHora().toString());
                    }

                    writer.write("---");
                    writer.newLine();
                }
            }
        } catch (IOException e) {
            throw new IllegalStateException("Falha ao salvar solicitacoes em arquivo TXT.", e);
        }
    }

    public List<Solicitacao> carregar(String nomeArquivo) {
        Path caminho = Paths.get(nomeArquivo);
        if (!Files.exists(caminho)) {
            return new ArrayList<>();
        }

        try {
            List<String> linhas = Files.readAllLines(caminho, StandardCharsets.UTF_8);
            List<Solicitacao> carregadas = new ArrayList<>();
            Map<String, String> bloco = new LinkedHashMap<>();

            for (String linha : linhas) {
                if (linha == null || linha.trim().isEmpty()) {
                    continue;
                }

                if ("---".equals(linha.trim())) {
                    if (!bloco.isEmpty()) {
                        carregadas.add(mapearBloco(bloco));
                        bloco.clear();
                    }
                    continue;
                }

                int idx = linha.indexOf('=');
                if (idx <= 0) {
                    continue;
                }

                // Remove BOM para arquivos criados/alterados por alguns editores no Windows.
                String chave = linha.substring(0, idx).replace("\uFEFF", "");
                String valor = linha.substring(idx + 1);
                bloco.put(chave, desescapar(valor));
            }

            if (!bloco.isEmpty()) {
                carregadas.add(mapearBloco(bloco));
            }

            return carregadas;
        } catch (Exception e) {
            throw new IllegalStateException("Falha ao carregar solicitacoes do arquivo TXT.", e);
        }
    }

    private Solicitacao mapearBloco(Map<String, String> bloco) {
        // Reconstrói objeto completo (incluindo histórico) a partir do bloco de texto.
        Categoria categoria = Categoria.valueOf(valorObrigatorio(bloco, "categoria"));
        Prioridade prioridade = Prioridade.valueOf(valorObrigatorio(bloco, "prioridade"));

        String nomeSolicitante = valorOpcional(bloco, "solicitanteNome");
        String contatoSolicitante = valorOpcional(bloco, "solicitanteContato");
        Usuario solicitante = (nomeSolicitante == null || nomeSolicitante.trim().isEmpty() || "Anônimo".equalsIgnoreCase(nomeSolicitante) || "Anonimo".equalsIgnoreCase(nomeSolicitante))
                ? new Usuario()
                : new Usuario(nomeSolicitante, contatoSolicitante);

        Solicitacao solicitacao = new Solicitacao(
                categoria,
                valorOpcional(bloco, "descricao"),
                valorOpcional(bloco, "localizacao"),
                prioridade,
                solicitante
        );

        solicitacao.setTitle(valorOpcional(bloco, "title"));
        definirCampo(solicitacao, "protocolo", valorOpcional(bloco, "protocolo"));
        definirCampo(solicitacao, "status", StatusSolicitacao.valueOf(valorObrigatorio(bloco, "status")));
        definirCampo(solicitacao, "dataAbertura", LocalDateTime.parse(valorObrigatorio(bloco, "dataAbertura")));
        definirCampo(solicitacao, "prazoAlvo", LocalDateTime.parse(valorObrigatorio(bloco, "prazoAlvo")));

        int historicoCount = parseInteiro(valorOpcional(bloco, "historicoCount"));
        List<HistoricoStatus> historico = new ArrayList<>();
        for (int i = 0; i < historicoCount; i++) {
            HistoricoStatus item = new HistoricoStatus(
                    StatusSolicitacao.valueOf(valorObrigatorio(bloco, "h." + i + ".statusAnterior")),
                    StatusSolicitacao.valueOf(valorObrigatorio(bloco, "h." + i + ".statusNovo")),
                    valorOpcional(bloco, "h." + i + ".comentario"),
                    valorOpcional(bloco, "h." + i + ".responsavel")
            );
            definirCampo(item, "dataHora", LocalDateTime.parse(valorObrigatorio(bloco, "h." + i + ".dataHora")));
            historico.add(item);
        }
        definirCampo(solicitacao, "historico", historico);

        return solicitacao;
    }

    private int parseInteiro(String valor) {
        if (valor == null || valor.trim().isEmpty()) {
            return 0;
        }
        return Integer.parseInt(valor.trim());
    }

    private String valorObrigatorio(Map<String, String> bloco, String chave) {
        String valor = bloco.get(chave);
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalStateException("Campo obrigatorio ausente no TXT: " + chave);
        }
        return valor;
    }

    private String valorOpcional(Map<String, String> bloco, String chave) {
        String valor = bloco.get(chave);
        return (valor == null || valor.isEmpty()) ? null : valor;
    }

    private void escreverLinha(BufferedWriter writer, String chave, String valor) throws IOException {
        writer.write(chave + "=" + escapar(valor));
        writer.newLine();
    }

    private String escapar(String valor) {
        if (valor == null) {
            return "";
        }
        return valor
                .replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\r", "\\r");
    }

    private String desescapar(String valor) {
        if (valor == null) {
            return null;
        }
        StringBuilder resultado = new StringBuilder();
        for (int i = 0; i < valor.length(); i++) {
            char atual = valor.charAt(i);
            if (atual == '\\' && i + 1 < valor.length()) {
                char proximo = valor.charAt(++i);
                switch (proximo) {
                    case 'n':
                        resultado.append('\n');
                        break;
                    case 'r':
                        resultado.append('\r');
                        break;
                    case '\\':
                        resultado.append('\\');
                        break;
                    default:
                        resultado.append(proximo);
                        break;
                }
            } else {
                resultado.append(atual);
            }
        }
        return resultado.toString();
    }

    private void definirCampo(Object alvo, String nomeCampo, Object valor) {
        try {
            // Usado para restaurar campos de estado que não têm setter público.
            Field campo = alvo.getClass().getDeclaredField(nomeCampo);
            campo.setAccessible(true);
            campo.set(alvo, valor);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException("Falha ao mapear campo: " + nomeCampo, e);
        }
    }
}

