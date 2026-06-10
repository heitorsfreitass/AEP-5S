# AEP-5S — ObservaAção

Sistema de registro e acompanhamento de solicitações urbanas desenvolvido para a AEP do curso de Engenharia de Software (ESOFT5S).

---

## Branches

| Branch | Descrição |
|---|---|
| `master` | Versão CLI em Java puro (1ª entrega) |
| `testespring` | Versão web com Spring Boot + H2 + Thymeleaf (2ª entrega) |

---

## Versão Web — Spring Boot (branch `testespring`)

### Tecnologias

- Java 17
- Spring Boot 3.2.5
- Spring Data JPA
- Banco H2 (in-memory)
- Thymeleaf
- Bootstrap 5.3

### Funcionalidades

- Dashboard com resumo geral (total, abertas, em execução, resolvidas)
- Nova solicitação (identificado ou anônimo, com categoria, prioridade, localização)
- Consulta por protocolo com histórico de status em timeline
- Painel do servidor com filtros por status, prioridade, categoria e bairro
- Atualização de status via modal com comentário obrigatório
- Fila de atendimento ordenada por SLA (prazo alvo)
- Dados de exemplo carregados automaticamente ao iniciar

### Pré-requisitos

- JDK 17 ou superior instalado
- Maven (ou usar o `mvnw` incluso no projeto)

### Como rodar

```bash
# 1. Clone o repositório
git clone https://github.com/heitorsfreitass/AEP-5S.git
cd AEP-5S

# 2. Mude para a branch
git checkout testespring

# 3. Entre na pasta do projeto Spring
cd web

# 4. Rode com Maven Wrapper (não precisa ter Maven instalado)
./mvnw spring-boot:run       # Linux / Mac
./mvnw.cmd spring-boot:run   # Windows
```

> **Windows (PowerShell):**
> ```powershell
> cd web
> ./mvnw.cmd spring-boot:run
> ```

5. Acesse no navegador: **http://localhost:8080**

### Telas disponíveis

| Rota | Tela |
|---|---|
| `/` | Dashboard |
| `/solicitacao` | Abrir nova solicitação |
| `/protocolo?protocolo=OA-...` | Consultar protocolo |
| `/servidor` | Painel do servidor |
| `/sla` | Fila de atendimento (SLA) |
| `/h2-console` | Console do banco H2 (dev) |

### Estrutura do projeto web

```
web/
├── src/main/java/com/aep5s/
│   ├── WebApplication.java
│   ├── config/          # DataSeeder (dados de exemplo)
│   ├── controllers/     # SolicitacaoController, HomeController, UsuarioController
│   ├── services/        # SolicitacaoService, UsuarioService, FilaAtendimentoService
│   ├── repositories/    # SolicitacaoRepository, UsuarioRepository
│   ├── models/          # Solicitacao, Usuario, HistoricoStatus
│   └── enums/           # Categoria, Prioridade, StatusSolicitacao
└── src/main/resources/
    ├── application.properties
    ├── static/css/style.css
    └── templates/       # dashboard, solicitacao, protocolo, servidor, sla, sucesso
```

---

## Versão CLI — Java puro (branch `master`)

Interface de console para registro e acompanhamento de solicitações.

### Como executar (Windows)

```powershell
git checkout master
New-Item -ItemType Directory -Path ".\bin" -Force | Out-Null
javac -d ".\bin" (Get-ChildItem -Path ".\src" -Recurse -Filter "*.java").FullName
java -cp ".\bin" Main
```

---

## Regras de negócio

### Categorias
`ILUMINACAO`, `BURACO`, `LIMPEZA`, `SAUDE`, `PODA`, `VAZAMENTO`, `ASSEDIO`, `SEGURANCA`, `OUTRO`

### Prioridades e SLA
| Prioridade | Prazo alvo |
|---|---|
| URGENTE | 4 horas |
| ALTA | 24 horas |
| MEDIA | 48 horas |
| BAIXA | 72 horas |

### Fluxo de status
`ABERTO` → `TRIAGEM` → `EM_EXECUCAO` → `RESOLVIDO` → `ENCERRADO`

---

## Licença

Projeto acadêmico — ESOFT5S / 2026.
