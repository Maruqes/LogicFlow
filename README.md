# Logic Flow

Logic Flow é uma aplicação desenvolvida com **Spring Boot** para gerir fluxos lógicos de informações e processos. Este projeto utiliza **PostgreSQL** como base de dados e está configurado com **Gradle** como sistema de build.

---

## Funcionalidades
- Gestão de utilizadores com autenticação via token.
- Manipulação de dados dinâmicos para fluxos lógicos.
- APIs REST para comunicação eficiente com o frontend.
- Configurações seguras para integrações futuras.

---

## Tecnologias Utilizadas
- **Java 21**: Linguagem principal do projeto.
- **Spring Boot 3.4.1**: Framework para desenvolvimento backend.
- **PostgreSQL**: Base de dados relacional.
- **Gradle**: Sistema de build e gestão de dependências.
- **NGINX** (opcional): Proxy reverso para servir a aplicação.

---

## Requisitos
Certifica-te de que tens os seguintes programas instalados:

- Java 21 ou superior
- Gradle
- PostgreSQL 14 ou superior
- NGINX (se aplicável)

---

## Configuração

### 1. Clonar o Repositório
```bash
git clone <url-do-repositorio>
cd LogicFlow
```

### 2. Configurar o PostgreSQL
1. Cria uma base de dados chamada `logicflow`.
2. Atualiza as credenciais no ficheiro `application.properties` ou `application.yml`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/logicflow
spring.datasource.username=<teu-username>
spring.datasource.password=<tua-password>
```

### 3. Construir o Projeto
Executa o comando abaixo para fazer build da aplicação:
```bash
./gradlew build
```

### 4. Iniciar a Aplicação
Executa a aplicação com:
```bash
./gradlew bootRun
```
A aplicação estará disponível em [http://localhost:8080](http://localhost:8080) por padrão.

---

## Endpoints Principais
### 1. Testar Conectividade
**Endpoint:** `/ping`
- **Método:** GET
- **Parâmetros:**
  - `username`: Nome do utilizador
  - `token`: Token de autenticação
- **Exemplo de Resposta:**
  ```json
  {
      "message": "pong"
  }
  ```

---

## Estrutura do Projeto

```
LogicFlow/
├── Server/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/
│   │   │   │   ├── LogicFlow/
│   │   │   │   │   └── LogicFlowApplication.java
│   │   │   ├── resources/
│   │   │       └── application.properties
└── build.gradle
```

---

## Licença
Este projeto é distribuído sob a licença GPL-3.0 license. Consulta o ficheiro `LICENSE` para mais informações.

---

## Autor
Criado por **Gonçalo Marques** e **João Barbosa**. Para dúvidas ou sugestões, entra em contacto!
