package com.aep5s.config;

import com.aep5s.enums.Categoria;
import com.aep5s.enums.Prioridade;
import com.aep5s.enums.StatusSolicitacao;
import com.aep5s.models.Solicitacao;
import com.aep5s.models.Usuario;
import com.aep5s.repositories.SolicitacaoRepository;
import com.aep5s.repositories.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final SolicitacaoRepository solicitacaoRepo;
    private final UsuarioRepository usuarioRepo;

    public DataSeeder(SolicitacaoRepository solicitacaoRepo, UsuarioRepository usuarioRepo) {
        this.solicitacaoRepo = solicitacaoRepo;
        this.usuarioRepo = usuarioRepo;
    }

    @Override
    public void run(String... args) {
        if (solicitacaoRepo.count() > 0) return;

        Usuario joao     = usuarioRepo.save(new Usuario("João Silva", "joao@email.com"));
        Usuario maria    = usuarioRepo.save(new Usuario("Maria Souza", "maria@email.com"));
        Usuario carlos   = usuarioRepo.save(new Usuario("Carlos Lima", "carlos@email.com"));
        Usuario fernanda = usuarioRepo.save(new Usuario("Fernanda Alves", "fernanda@email.com"));
        Usuario anonimo  = usuarioRepo.save(Usuario.anonimo());

        Solicitacao s1 = new Solicitacao(Categoria.BURACO, "Buraco grande na rua principal em frente ao número 123, dificultando o trânsito de veículos.", "Centro", Prioridade.ALTA, joao);
        s1.setTitle("Buraco na via");
        s1.atualizarStatus(StatusSolicitacao.TRIAGEM, "Solicitação em triagem pela equipe.", "Sistema");
        s1.atualizarStatus(StatusSolicitacao.EM_EXECUCAO, "Serviço em execução.", "Equipe Obras");
        solicitacaoRepo.save(s1);

        Solicitacao s2 = new Solicitacao(Categoria.ILUMINACAO, "Poste com lâmpada queimada há mais de 2 semanas.", "Jardim das Flores", Prioridade.MEDIA, maria);
        s2.setTitle("Iluminação pública");
        s2.atualizarStatus(StatusSolicitacao.TRIAGEM, "Em triagem.", "Sistema");
        solicitacaoRepo.save(s2);

        Solicitacao s3 = new Solicitacao(Categoria.LIMPEZA, "Acúmulo de lixo no terreno baldio da esquina.", "Vila Nova", Prioridade.BAIXA, anonimo);
        s3.setTitle("Limpeza urbana");
        solicitacaoRepo.save(s3);

        Solicitacao s4 = new Solicitacao(Categoria.PODA, "Árvore com galhos sobre a fiação elétrica.", "Bela Vista", Prioridade.ALTA, carlos);
        s4.setTitle("Poda de árvore");
        solicitacaoRepo.save(s4);

        Solicitacao s5 = new Solicitacao(Categoria.VAZAMENTO, "Vazamento de água na calçada há dias.", "Centro", Prioridade.URGENTE, fernanda);
        s5.setTitle("Vazamento de água");
        s5.atualizarStatus(StatusSolicitacao.TRIAGEM, "Em triagem urgente.", "Sistema");
        s5.atualizarStatus(StatusSolicitacao.EM_EXECUCAO, "Equipe de saneamento acionada.", "SAAE");
        solicitacaoRepo.save(s5);

        Solicitacao s6 = new Solicitacao(Categoria.SAUDE, "Esgoto a céu aberto na Rua das Acácias.", "São Pedro", Prioridade.ALTA, joao);
        s6.setTitle("Esgoto a céu aberto");
        s6.atualizarStatus(StatusSolicitacao.TRIAGEM, "Triagem concluída.", "Sistema");
        s6.atualizarStatus(StatusSolicitacao.EM_EXECUCAO, "Obras iniciadas.", "Equipe Obras");
        s6.atualizarStatus(StatusSolicitacao.RESOLVIDO, "Problema solucionado.", "Equipe Obras");
        solicitacaoRepo.save(s6);
    }
}
