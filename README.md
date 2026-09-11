# Axii Crypto

Projeto acadêmico (FIAP) de integração Java + Oracle Database, modelando uma
plataforma de gestão de criptoativos.

## Estrutura do repositório

```bash
📂Axii Crypto
  │
  ├─ 📂classes java
  │    ├─ 📁src/main
  │    │    ├─ 📁java/br.com.fiap
  │    │    │    ├─ 📁dao
  │    │    │    ├─ 📁exception
  │    │    │    ├─ 📁factory
  │    │    │    ├─ 📁model
  │    │    │    ├─ 📁view
  │    │    │    └─ 📄App.java
  │    │    └─ 📁env
  │    └─ 📄pom.xml
  │
  └─ 📂scripts sql
       ├─ 📄SCRIPT_DDL_PROJETO_AXII.sql
       └─ 📄SCRIPT_DML_PROJETO_AXII.sql
```

## Pré-requisitos

> [!NOTE]  
> **Tip**: Antes de iniciar, você precisa:
>
> - JDK 17+
> - Maven
> - Acesso ao Oracle Database da FIAP (`oracle.fiap.com.br:1521:orcl`) com seu
>   usuário (RM) e senha

## Configurando o banco de dados

1. No SQL Developer (ou ferramenta equivalente), conecte com seu usuário e
   execute, nessa ordem:
   - `axii crypto - scripts sql/SCRIPT_DDL_PROJETO_AXII.sql` — cria as tabelas
     e constraints.
   - `axii crypto - scripts sql/SCRIPT_DML_PROJETO_AXII.sql` — popula as
     tabelas e traz exemplos de INSERT, UPDATE, DELETE e SELECT.

   Na primeira execução do DDL, os comandos `DROP TABLE` iniciais vão gerar o
   erro `ORA-00942` — é esperado, pois as tabelas ainda não existem.

## Configurando as credenciais do Java

As credenciais **não** ficam no código-fonte, para não expor usuário/senha de
ninguém que abrir o repositório.

1. Abra o arquivo:
   ```
   axii crypto - classes java/src/main/env/db.properties
   ```
2. Preencha com o seu usuário e senha do Oracle FIAP:
   ```properties
   db.url=jdbc:oracle:thin:@oracle.fiap.com.br:1521:orcl
   db.user=SEU_RM_AQUI
   db.password=SUA_SENHA_AQUI
   ```

> [!WARNING]
> O arquivo `db.properties` está no `.gitignore` e não deve ser commitado com
> credenciais reais. Antes de compartilhar ou entregar o projeto, limpe os
> valores de `db.user` e `db.password`.

## Executando

Dentro de `axii crypto - classes java/`:

```
mvn compile exec:java -Dexec.mainClass="br.com.fiap.App"
```

A classe `App` executa, em sequência, o cadastro, a listagem, a pesquisa, a
atualização e a remoção de um usuário de teste, usando as classes em
`br.com.fiap.view` (`SignInUser`, `ListAllUsers`, `SearchUser`, `UpdateUser`,
`DeleteUser`).

## Observações

- Apenas a entidade `User` (e seus relacionamentos 1:1 com `Settings`,
  `Notifications` e `Identity`) está integrada ao banco nesta fase.
- A senha do usuário é armazenada em texto puro no banco; em um projeto real,
  deveria ser armazenada com hash.
