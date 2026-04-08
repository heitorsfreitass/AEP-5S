# AEP-5S

Sistema em Java para registro e acompanhamento de solicitações urbanas via console.

## Visão geral

O projeto simula o fluxo de atendimento entre cidadão e servidor público:

- o cidadão abre solicitações e consulta o andamento;
- o servidor atualiza status e acompanha a fila por SLA;
- os dados ficam em memória durante a execução e são persistidos em `solicitacoes.txt`.

## Funcionalidades

- Cadastro de solicitações pelo cidadão (identificado ou anônimo)
- Consulta por protocolo
- Atualização de status pelo servidor
- Filtros por prioridade, categoria e bairro
- Fila de atendimento com foco em SLA
- Carga inicial automática de dados (seeder)

## Tecnologias usadas

- Java
- JDK (compilação e execução)
- Interface de console
- Persistência em arquivo TXT

## Regras principais

### Categorias

Aceita **número ou nome** no cadastro:

- `ILUMINACAO`, `BURACO`, `LIMPEZA`, `SAUDE`, `ASSEDIO`, `INJURIA`, `CRIME`, `OUTRO`

### Prioridades

- `BAIXA` (72h)
- `MEDIA` (48h)
- `ALTA` (24h)
- `URGENTE` (4h)

### Status

- `ABERTO`
- `TRIAGEM`
- `EM_EXECUCAO`
- `RESOLVIDO`
- `ENCERRADO`

## Estrutura (resumo)

```text
src/
├── Main.java
├── UI/              # Menus e interação no console
├── Services/        # Regras de negócio
├── Repositories/    # Dados em memória
├── Storage/         # Leitura/gravação em TXT
├── Models/          # Entidades
└── Enums/           # Categoria, prioridade e status
```

## Requisitos

- JDK instalado
- Terminal (PowerShell, CMD ou similar)

## Como executar (Windows)

1. Clone o repositorio:

```powershell
git clone https://github.com/heitorsfreitass/AEP-5S.git
```

2. Entre na pasta do projeto clonado e execute no PowerShell:

```powershell
New-Item -ItemType Directory -Path ".\bin" -Force | Out-Null; javac -d ".\bin" (Get-ChildItem -Path ".\src" -Recurse -Filter "*.java").FullName; java -cp ".\bin" Main
```

## Fluxo rapido de uso

1. Cidadao registra uma solicitacao no painel de atendimento.
2. Servidor consulta o protocolo e atualiza o status.
3. Sistema ordena a fila por SLA e salva os dados ao sair.

## Persistência

- O sistema tenta carregar `solicitacoes.txt` na inicialização.
- Se o arquivo estiver ausente/vazio, o sistema cria dados iniciais automaticamente.
- Ao sair, salva as solicitações no mesmo arquivo.

> Execute o sistema a partir da pasta `AEP-5S` para manter o arquivo no local esperado.

## Licença

Projeto acadêmico/educacional.
