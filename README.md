# Projeto Final - Backend E-commerce

API REST para o fluxo completo de e-commerce com autenticacao JWT, perfis de acesso e regras de negocio para cliente e vendedor.

## Sumario

- Visao geral
- Stack e arquitetura
- Configuracao e execucao
- Seguranca e autorizacao
- Endpoints
- Regras de negocio
- Relacionamentos entre entidades
- Padrao de erros

## Visao geral

Este backend cobre:

- Cadastro e login de cliente/vendedor
- Produtos, categorias e estoque
- Carrinho e pedidos
- Pagamentos e entregas
- Avaliacoes
- Cupons por produto

## Stack e arquitetura

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (jjwt)
- MySQL
- Maven Wrapper

Estrutura principal:

- `ecommerce/src/main/java/com/ProjetoFinal/ecommerce/controller`
- `ecommerce/src/main/java/com/ProjetoFinal/ecommerce/service`
- `ecommerce/src/main/java/com/ProjetoFinal/ecommerce/repository`
- `ecommerce/src/main/java/com/ProjetoFinal/ecommerce/model`
- `ecommerce/src/main/java/com/ProjetoFinal/ecommerce/config`

## Configuracao e execucao

Requisitos:

- Java 21+
- MySQL 8+

Variaveis de ambiente (arquivo opcional `ecommerce/.env`):

```env
DB_URL=jdbc:mysql://localhost:3306/ecommerce?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=senha
JWT_SECRET=uma-chave-secreta-com-pelo-menos-256-bits
```

Comandos:

```bash
cd ecommerce
./mvnw spring-boot:run
./mvnw -DskipTests compile
./mvnw -DskipTests package
```

## Seguranca e autorizacao

Autenticacao via JWT (Bearer Token):

```http
Authorization: Bearer <token>
```

Rotas publicas:

- `/api/auth/**`
- `GET /api/produtos/**`
- `GET /api/categorias/**`
- `GET /api/avaliacoes/produto/**`
- `/error`

Rotas de cliente (`ROLE_CLIENTE`):

- `/api/clientes/**`
- `/api/carrinho/**`
- `POST /api/pedidos/cliente/**`
- `GET /api/pedidos/cliente/**`
- `/api/pagamentos/**`
- `POST /api/avaliacoes/**`
- `POST /api/cupons/validar`
- `POST /api/cupons/consumir`

Rotas de vendedor (`ROLE_VENDEDOR`):

- `/api/vendedores/**`
- `/api/usuarios/**`
- `/api/estoques/**`
- `/api/cupons/**` (exceto validar/consumir)
- `POST|PUT|DELETE /api/produtos/**`
- `GET /api/produtos/vendedor/**`
- `GET /api/pedidos/vendedor/**`
- `PATCH /api/entregas/**`

## Endpoints

Auth:

- `POST /api/auth/login`
- `POST /api/auth/register/cliente`
- `POST /api/auth/register/vendedor`

Usuarios:

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

Clientes:

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

Vendedores:

- `POST /api/vendedores`
- `GET /api/vendedores`
- `GET /api/vendedores/{id}`
- `PUT /api/vendedores/{id}`
- `DELETE /api/vendedores/{id}`

Produtos:

- `POST /api/produtos`
- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `GET /api/produtos/vendedor/{vendedorId}`
- `GET /api/produtos/categoria/{categoriaId}`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

Categorias:

- `POST /api/categorias`
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

Estoques:

- `POST /api/estoques`
- `GET /api/estoques`
- `GET /api/estoques/{id}`
- `GET /api/estoques/produto/{produtoId}`
- `PUT /api/estoques/{id}`
- `DELETE /api/estoques/{id}`

Carrinho:

- `GET /api/carrinho/{clienteId}`
- `POST /api/carrinho/{clienteId}/itens?produtoId={id}&quantidade={n}`
- `DELETE /api/carrinho/{clienteId}/itens/{produtoId}`
- `DELETE /api/carrinho/{clienteId}/limpar`

Pedidos:

- `POST /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `GET /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos/vendedor/{vendedorId}`
- `PATCH /api/pedidos/{id}/status?status={STATUS}`
- `PATCH /api/pedidos/{id}/cancelar`

Entregas:

- `POST /api/entregas/pedido/{pedidoId}`
- `GET /api/entregas`
- `GET /api/entregas/{id}`
- `GET /api/entregas/pedido/{pedidoId}`
- `PATCH /api/entregas/{id}/status?status={STATUS}`
- `DELETE /api/entregas/{id}`

Pagamentos:

- `POST /api/pagamentos/pedido/{pedidoId}`
- `GET /api/pagamentos`
- `GET /api/pagamentos/{id}`
- `DELETE /api/pagamentos/{id}`

Avaliacoes:

- `POST /api/avaliacoes?pedidoId={id}`
- `GET /api/avaliacoes/produto/{produtoId}`
- `GET /api/avaliacoes/cliente/{clienteId}`
- `GET /api/avaliacoes/{id}`
- `GET /api/avaliacoes/produto/{produtoId}/media`
- `DELETE /api/avaliacoes/{id}`

Cupons:

- `POST /api/cupons`
- `GET /api/cupons`
- `GET /api/cupons/{id}`
- `GET /api/cupons/vendedor/{vendedorId}`
- `PUT /api/cupons/{id}`
- `DELETE /api/cupons/{id}`
- `POST /api/cupons/validar?codigo={CODIGO}&produtoId={ID}`
- `POST /api/cupons/consumir?codigo={CODIGO}&produtoId={ID}`

## Regras de negocio

- Email e CPF unicos no cadastro.
- Senha armazenada com BCrypt.
- Categoria com nome unico.
- Reserva de estoque so com quantidade suficiente.
- Carrinho nao aceita quantidade <= 0.
- Vendedor nao pode comprar produto proprio.
- Pedido nao pode ser criado com carrinho vazio.
- Endereco obrigatorio no checkout.
- Cancelamento proibido para pedido enviado/entregue.
- Avaliacao somente para pedido entregue e produto do pedido.
- Cliente nao avalia o mesmo produto duas vezes.
- Cupom e vinculado a produto especifico.
- Cupom so e consumido apos finalizacao da compra.

Status de pedido:

- `AGUARDANDO_PAGAMENTO`
- `PAGO`
- `EM_PREPARACAO`
- `ENVIADO`
- `ENTREGUE`
- `CANCELADO`

## Relacionamentos entre entidades

- `Cliente 1:N Endereco`
- `Cliente 1:1 Carrinho`
- `Pedido N:1 Cliente`
- `Pedido 1:N ItemPedido`
- `Pedido 1:1 Pagamento`
- `Pedido 1:1 Entrega`
- `ItemPedido N:1 Produto`
- `ItemCarrinho N:1 Carrinho`
- `ItemCarrinho N:1 Produto`
- `Produto N:1 Categoria`
- `Produto N:1 Vendedor`
- `Produto 1:1 Estoque`
- `Produto 1:N Avaliacao`
- `Avaliacao N:1 Cliente`
- `Avaliacao N:1 Produto`
- `Cupom N:1 Produto`
- `Entrega N:1 Endereco`

## Padrao de erros

Formato:

```json
{
  "status": 422,
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "Mensagem de erro",
  "details": {},
  "timestamp": "2026-04-27T10:00:00"
}
```

Mapeamento:

- `NoSuchElementException` -> `404 RESOURCE_NOT_FOUND`
- `IllegalArgumentException` -> `400 BAD_REQUEST`
- `IllegalStateException` -> `422 BUSINESS_RULE_VIOLATION`
- Excecao generica -> `500 INTERNAL_SERVER_ERROR`

## Checklist de testes sugeridos

- Login cliente e vendedor
- Fluxo completo de carrinho e checkout
- Fluxo vendedor (`PAGO -> ENVIADO -> ENTREGUE`)
- Cancelamento em status permitidos
- Avaliacao somente em pedido entregue
- Cupom por produto e consumo apos compra
- Build backend: `./mvnw -DskipTests package`

## Observacoes finais

- API stateless com JWT.
- CORS habilitado para desenvolvimento.
- `ddl-auto=update` recomendado apenas para desenvolvimento local.
- Em producao, usar migracoes versionadas (Flyway/Liquibase).
# Projeto Final - Backend E-commerce

API REST de e-commerce desenvolvida com Spring Boot, com autenticacao JWT, controle de acesso por perfil, regras de negocio para compra/venda e integracao completa com frontend.

---

## Sumario

- [Visao Geral](#visao-geral)
- [Stack Tecnologica](#stack-tecnologica)
- [Arquitetura e Estrutura](#arquitetura-e-estrutura)
- [Requisitos](#requisitos)
- [Configuracao de Ambiente](#configuracao-de-ambiente)
- [Como Executar](#como-executar)
- [Autenticacao e Seguranca](#autenticacao-e-seguranca)
- [Matriz de Autorizacao](#matriz-de-autorizacao)
- [Endpoints da API](#endpoints-da-api)
- [Regras de Negocio](#regras-de-negocio)
- [Relacionamentos entre Entidades](#relacionamentos-entre-entidades)
- [Padrao de Erros](#padrao-de-erros)
- [Checklist de Testes Sugeridos](#checklist-de-testes-sugeridos)
- [Observacoes Finais](#observacoes-finais)

---

## Visao Geral

Este backend implementa os principais fluxos de um marketplace:

- Cadastro e login de cliente e vendedor
- Gerenciamento de produtos, categorias e estoque
- Carrinho de compras
- Criacao e ciclo de vida de pedidos
- Processamento de pagamentos
- Entregas
- Avaliacoes de produtos
- Cupons de desconto por produto

---

## Stack Tecnologica

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (jjwt)
- MySQL
- Lombok
- Maven Wrapper

---

## Arquitetura e Estrutura

```text
Projeto-Final-ArqObj-backend/
├── README.md
└── ecommerce/
    ├── pom.xml
    ├── src/main/resources/application.properties
    └── src/main/java/com/ProjetoFinal/ecommerce/
        ├── config/
        ├── controller/
        ├── service/
        ├── repository/
        ├── model/
        └── dto/
```

Organizacao por camadas:

- `controller`: exposicao REST
- `service`: regras de negocio
- `repository`: acesso ao banco
- `model`: entidades JPA
- `config/security`: autenticacao JWT e autorizacao por papel

---

## Requisitos

- Java 21+
- MySQL 8+
- Maven (opcional, pois existe `./mvnw`)

---

## Configuracao de Ambiente

A aplicacao le variaveis via:

- `application.properties` com placeholders
- arquivo opcional `.env` na pasta `ecommerce/`

Exemplo de `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/ecommerce?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=senha
JWT_SECRET=uma-chave-secreta-com-pelo-menos-256-bits
```

Configuracao atual relevante:

- Porta: `8080`
- JPA: `ddl-auto=update`
- SQL log habilitado

---

## Como Executar

Entre na pasta do backend:

```bash
cd ecommerce
```

### Rodar em desenvolvimento

```bash
./mvnw spring-boot:run
```

### Compilar

```bash
./mvnw -DskipTests compile
```

### Gerar pacote

```bash
./mvnw -DskipTests package
```

---

## Autenticacao e Seguranca

A API utiliza JWT stateless.

### Login

`POST /api/auth/login`

Body:

```json
{
  "email": "cliente@email.com",
  "senha": "123456"
}
```

Resposta:

```json
{
  "token": "jwt-token",
  "tipo": "CLIENTE",
  "id": 1,
  "email": "cliente@email.com",
  "nome": "Cliente"
}
```

### Uso do token

Enviar em rotas protegidas:

```http
Authorization: Bearer <token>
```

---

## Matriz de Autorizacao

### Publicas

- `/api/auth/**`
- `GET /api/produtos/**`
- `GET /api/categorias/**`
- `GET /api/avaliacoes/produto/**`
- `/error`

### `ROLE_CLIENTE`

- `/api/clientes/**`
- `/api/carrinho/**`
- `POST /api/pedidos/cliente/**`
- `GET /api/pedidos/cliente/**`
- `/api/pagamentos/**`
- `POST /api/avaliacoes/**`
- `POST /api/cupons/validar`
- `POST /api/cupons/consumir`

### `ROLE_VENDEDOR`

- `/api/vendedores/**`
- `/api/usuarios/**`
- `/api/estoques/**`
- `/api/cupons/**` (exceto validar/consumir)
- `POST|PUT|DELETE /api/produtos/**`
- `GET /api/produtos/vendedor/**`
- `GET /api/pedidos/vendedor/**`
- `PATCH /api/entregas/**`

### Autenticado (qualquer papel)

- `PATCH /api/pedidos/**`
- `GET /api/entregas/pedido/**`
- Demais rotas nao publicas

---

## Endpoints da API

### Auth

- `POST /api/auth/login`
- `POST /api/auth/register/cliente`
- `POST /api/auth/register/vendedor`

### Usuarios

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

### Clientes

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

### Vendedores

- `POST /api/vendedores`
- `GET /api/vendedores`
- `GET /api/vendedores/{id}`
- `PUT /api/vendedores/{id}`
- `DELETE /api/vendedores/{id}`

### Produtos

- `POST /api/produtos`
- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `GET /api/produtos/vendedor/{vendedorId}`
- `GET /api/produtos/categoria/{categoriaId}`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

### Categorias

- `POST /api/categorias`
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

### Estoques

- `POST /api/estoques`
- `GET /api/estoques`
- `GET /api/estoques/{id}`
- `GET /api/estoques/produto/{produtoId}`
- `PUT /api/estoques/{id}`
- `DELETE /api/estoques/{id}`

### Carrinho

- `GET /api/carrinho/{clienteId}`
- `POST /api/carrinho/{clienteId}/itens?produtoId={id}&quantidade={n}`
- `DELETE /api/carrinho/{clienteId}/itens/{produtoId}`
- `DELETE /api/carrinho/{clienteId}/limpar`

### Pedidos

- `POST /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `GET /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos/vendedor/{vendedorId}`
- `PATCH /api/pedidos/{id}/status?status={STATUS}`
- `PATCH /api/pedidos/{id}/cancelar`

### Entregas

- `POST /api/entregas/pedido/{pedidoId}`
- `GET /api/entregas`
- `GET /api/entregas/{id}`
- `GET /api/entregas/pedido/{pedidoId}`
- `PATCH /api/entregas/{id}/status?status={STATUS}`
- `DELETE /api/entregas/{id}`

### Pagamentos

- `POST /api/pagamentos/pedido/{pedidoId}`
- `GET /api/pagamentos`
- `GET /api/pagamentos/{id}`
- `DELETE /api/pagamentos/{id}`

Tipos aceitos no `POST /api/pagamentos/pedido/{pedidoId}`:

- `PagamentoPix`
- `PagamentoCartao`
- `PagamentoBoleto`

### Avaliacoes

- `POST /api/avaliacoes?pedidoId={id}`
- `GET /api/avaliacoes/produto/{produtoId}`
- `GET /api/avaliacoes/cliente/{clienteId}`
- `GET /api/avaliacoes/{id}`
- `GET /api/avaliacoes/produto/{produtoId}/media`
- `DELETE /api/avaliacoes/{id}`

### Cupons

- `POST /api/cupons`
- `GET /api/cupons`
- `GET /api/cupons/{id}`
- `GET /api/cupons/vendedor/{vendedorId}`
- `PUT /api/cupons/{id}`
- `DELETE /api/cupons/{id}`
- `POST /api/cupons/validar?codigo={CODIGO}&produtoId={ID}`
- `POST /api/cupons/consumir?codigo={CODIGO}&produtoId={ID}`

---

## Regras de Negocio

### Cadastro e autenticacao

- Email e CPF devem ser unicos.
- Senha sempre armazenada com hash BCrypt.
- JWT obrigatorio para rotas protegidas.

### Produto, categoria e estoque

- Categoria com nome unico.
- Produto deve ter vendedor associado.
- Reserva de estoque falha com quantidade insuficiente.

### Carrinho

- Quantidade deve ser maior que zero.
- Vendedor nao pode comprar produto proprio.
- So adiciona item se houver estoque suficiente.

### Pedido e entrega

- Pedido nao pode ser criado com carrinho vazio.
- Endereco valido obrigatorio no checkout.
- Cancelamento proibido apos envio/entrega.
- Cancelamento devolve estoque e atualiza entrega.
- Status de pedido:
  - `AGUARDANDO_PAGAMENTO`
  - `PAGO`
  - `EM_PREPARACAO`
  - `ENVIADO`
  - `ENTREGUE`
  - `CANCELADO`

### Pagamento

- Apenas Pix, Cartao e Boleto.
- Processamento idempotente se pedido ja possui pagamento.

### Avaliacao

- Apenas comprador pode avaliar.
- Produto avaliado deve pertencer ao pedido informado.
- Pedido precisa estar entregue.
- Nota entre 1 e 5.
- Mesmo cliente nao avalia o mesmo produto duas vezes.

### Cupom

- Codigo unico.
- Vendedor so cria cupom para produto proprio.
- Cupom vinculado a um produto especifico.
- Cupom valido apenas se ativo, nao expirado, com uso disponivel e produto compativel.
- Consumo do cupom ocorre somente apos finalizacao da compra.

---

## Relacionamentos entre Entidades

- `Cliente 1:N Endereco`
- `Cliente 1:1 Carrinho`
- `Pedido N:1 Cliente`
- `Pedido 1:N ItemPedido`
- `Pedido 1:1 Pagamento`
- `Pedido 1:1 Entrega`
- `ItemPedido N:1 Produto`
- `ItemCarrinho N:1 Carrinho`
- `ItemCarrinho N:1 Produto`
- `Produto N:1 Categoria`
- `Produto N:1 Vendedor`
- `Produto 1:1 Estoque`
- `Produto 1:N Avaliacao`
- `Avaliacao N:1 Cliente`
- `Avaliacao N:1 Produto`
- `Cupom N:1 Produto`
- `Entrega N:1 Endereco`

Observacoes:

- Um cliente pode ter varios pedidos.
- Avaliacao e associada a cliente e produto (nao diretamente ao pedido).
- Cada pedido possui no maximo um pagamento e uma entrega.

---

## Padrao de Erros

Formato padrao:

```json
{
  "status": 422,
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "Mensagem de erro",
  "details": {},
  "timestamp": "2026-04-27T10:00:00"
}
```

Mapeamento de excecoes:

- `NoSuchElementException` -> `404 RESOURCE_NOT_FOUND`
- `IllegalArgumentException` -> `400 BAD_REQUEST`
- `IllegalStateException` -> `422 BUSINESS_RULE_VIOLATION`
- Excecao generica -> `500 INTERNAL_SERVER_ERROR`

---

## Checklist de Testes Sugeridos

- Login cliente e vendedor
- Fluxo de carrinho completo
- Checkout com endereco obrigatorio
- Pedido e pagamento concluidos
- Fluxo vendedor (`PAGO -> ENVIADO -> ENTREGUE`)
- Cancelamento em status permitidos
- Avaliacao somente para pedido entregue
- Cupom por produto e consumo apos compra
- Build backend:
  - `./mvnw -DskipTests package`

---

## Observacoes Finais

- API stateless com JWT.
- CORS habilitado para ambiente de desenvolvimento.
- `ddl-auto=update` recomendado apenas para desenvolvimento local.
- Para producao, prefira migracoes versionadas (ex.: Flyway/Liquibase).
# Projeto Final - Backend E-commerce

API REST de e-commerce desenvolvida com Spring Boot, com autenticação JWT, controle de acesso por perfil, regras de negócio para compra/venda e integração completa com frontend.

---

## Sumário

- [Visão Geral](#visão-geral)
- [Stack Tecnológica](#stack-tecnológica)
- [Arquitetura e Estrutura](#arquitetura-e-estrutura)
- [Requisitos](#requisitos)
- [Configuração de Ambiente](#configuração-de-ambiente)
- [Como Executar](#como-executar)
- [Autenticação e Segurança](#autenticação-e-segurança)
- [Matriz de Autorização](#matriz-de-autorização)
- [Endpoints da API](#endpoints-da-api)
- [Regras de Negócio](#regras-de-negócio)
- [Relacionamentos entre Entidades](#relacionamentos-entre-entidades)
- [Padrão de Erros](#padrão-de-erros)
- [Checklist de Testes Sugeridos](#checklist-de-testes-sugeridos)
- [Observações Finais](#observações-finais)

---

## Visão Geral

Este backend implementa os principais fluxos de um marketplace:

- Cadastro e login de cliente e vendedor
- Gerenciamento de produtos, categorias e estoque
- Carrinho de compras
- Criação e ciclo de vida de pedidos
- Processamento de pagamentos
- Entregas
- Avaliações de produtos
- Cupons de desconto por produto

---

## Stack Tecnológica

- **Java 21**
- **Spring Boot 4**
- **Spring Web MVC**
- **Spring Data JPA**
- **Spring Security**
- **JWT (jjwt)**
- **MySQL**
- **Lombok**
- **Maven Wrapper**

---

## Arquitetura e Estrutura

```text
Projeto-Final-ArqObj-backend/
├── README.md
└── ecommerce/
    ├── pom.xml
    ├── src/main/resources/application.properties
    └── src/main/java/com/ProjetoFinal/ecommerce/
        ├── config/
        ├── controller/
        ├── service/
        ├── repository/
        ├── model/
        └── dto/
```

Organização por camadas:

- `controller`: exposição REST
- `service`: regras de negócio
- `repository`: acesso ao banco
- `model`: entidades JPA
- `config/security`: autenticação JWT e autorização por papel

---

## Requisitos

- Java 21+
- MySQL 8+
- Maven (opcional, pois existe `./mvnw`)

---

## Configuração de Ambiente

A aplicação lê variáveis via:

- `application.properties` com placeholders
- arquivo opcional `.env` na pasta `ecommerce/`

Exemplo de `.env`:

```env
DB_URL=jdbc:mysql://localhost:3306/ecommerce?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=senha
JWT_SECRET=uma-chave-secreta-com-pelo-menos-256-bits
```

Configuração atual relevante:

- Porta: `8080`
- JPA: `ddl-auto=update`
- SQL log habilitado

---

## Como Executar

Entre na pasta do backend:

```bash
cd ecommerce
```

### Rodar em desenvolvimento

```bash
./mvnw spring-boot:run
```

### Compilar

```bash
./mvnw -DskipTests compile
```

### Gerar pacote

```bash
./mvnw -DskipTests package
```

---

## Autenticação e Segurança

A API utiliza **JWT stateless**.

### Login

`POST /api/auth/login`

Body:

```json
{
  "email": "cliente@email.com",
  "senha": "123456"
}
```

Resposta:

```json
{
  "token": "jwt-token",
  "tipo": "CLIENTE",
  "id": 1,
  "email": "cliente@email.com",
  "nome": "Cliente"
}
```

### Uso do token

Enviar em rotas protegidas:

```http
Authorization: Bearer <token>
```

---

## Matriz de Autorização

### Públicas

- `/api/auth/**`
- `GET /api/produtos/**`
- `GET /api/categorias/**`
- `GET /api/avaliacoes/produto/**`
- `/error`

### `ROLE_CLIENTE`

- `/api/clientes/**`
- `/api/carrinho/**`
- `POST /api/pedidos/cliente/**`
- `GET /api/pedidos/cliente/**`
- `/api/pagamentos/**`
- `POST /api/avaliacoes/**`
- `POST /api/cupons/validar`
- `POST /api/cupons/consumir`

### `ROLE_VENDEDOR`

- `/api/vendedores/**`
- `/api/usuarios/**`
- `/api/estoques/**`
- `/api/cupons/**` (exceto validar/consumir)
- `POST|PUT|DELETE /api/produtos/**`
- `GET /api/produtos/vendedor/**`
- `GET /api/pedidos/vendedor/**`
- `PATCH /api/entregas/**`

### Autenticado (qualquer papel)

- `PATCH /api/pedidos/**`
- `GET /api/entregas/pedido/**`
- Demais rotas não públicas

---

## Endpoints da API

## Auth

- `POST /api/auth/login`
- `POST /api/auth/register/cliente`
- `POST /api/auth/register/vendedor`

## Usuários

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

## Clientes

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

## Vendedores

- `POST /api/vendedores`
- `GET /api/vendedores`
- `GET /api/vendedores/{id}`
- `PUT /api/vendedores/{id}`
- `DELETE /api/vendedores/{id}`

## Produtos

- `POST /api/produtos`
- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `GET /api/produtos/vendedor/{vendedorId}`
- `GET /api/produtos/categoria/{categoriaId}`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

## Categorias

- `POST /api/categorias`
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

## Estoques

- `POST /api/estoques`
- `GET /api/estoques`
- `GET /api/estoques/{id}`
- `GET /api/estoques/produto/{produtoId}`
- `PUT /api/estoques/{id}`
- `DELETE /api/estoques/{id}`

## Carrinho

- `GET /api/carrinho/{clienteId}`
- `POST /api/carrinho/{clienteId}/itens?produtoId={id}&quantidade={n}`
- `DELETE /api/carrinho/{clienteId}/itens/{produtoId}`
- `DELETE /api/carrinho/{clienteId}/limpar`

## Pedidos

- `POST /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `GET /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos/vendedor/{vendedorId}`
- `PATCH /api/pedidos/{id}/status?status={STATUS}`
- `PATCH /api/pedidos/{id}/cancelar`

## Entregas

- `POST /api/entregas/pedido/{pedidoId}`
- `GET /api/entregas`
- `GET /api/entregas/{id}`
- `GET /api/entregas/pedido/{pedidoId}`
- `PATCH /api/entregas/{id}/status?status={STATUS}`
- `DELETE /api/entregas/{id}`

## Pagamentos

- `POST /api/pagamentos/pedido/{pedidoId}`
- `GET /api/pagamentos`
- `GET /api/pagamentos/{id}`
- `DELETE /api/pagamentos/{id}`

Tipos aceitos no `POST /api/pagamentos/pedido/{pedidoId}`:

- `PagamentoPix`
- `PagamentoCartao`
- `PagamentoBoleto`

## Avaliações

- `POST /api/avaliacoes?pedidoId={id}`
- `GET /api/avaliacoes/produto/{produtoId}`
- `GET /api/avaliacoes/cliente/{clienteId}`
- `GET /api/avaliacoes/{id}`
- `GET /api/avaliacoes/produto/{produtoId}/media`
- `DELETE /api/avaliacoes/{id}`

## Cupons

- `POST /api/cupons`
- `GET /api/cupons`
- `GET /api/cupons/{id}`
- `GET /api/cupons/vendedor/{vendedorId}`
- `PUT /api/cupons/{id}`
- `DELETE /api/cupons/{id}`
- `POST /api/cupons/validar?codigo={CODIGO}&produtoId={ID}`
- `POST /api/cupons/consumir?codigo={CODIGO}&produtoId={ID}`

---

## Regras de Negócio

## Cadastro e autenticação

- Email e CPF devem ser únicos.
- Senha sempre armazenada com hash BCrypt.
- JWT obrigatório para rotas protegidas.

## Produto, categoria e estoque

- Categoria com nome único.
- Produto deve ter vendedor associado.
- Reserva de estoque falha com quantidade insuficiente.

## Carrinho

- Quantidade deve ser maior que zero.
- Vendedor não pode comprar produto próprio.
- Só adiciona item se houver estoque suficiente.

## Pedido e entrega

- Pedido não pode ser criado com carrinho vazio.
- Endereço válido obrigatório no checkout.
- Cancelamento proibido após envio/entrega.
- Cancelamento devolve estoque e atualiza entrega.
- Status de pedido:
  - `AGUARDANDO_PAGAMENTO`
  - `PAGO`
  - `EM_PREPARACAO`
  - `ENVIADO`
  - `ENTREGUE`
  - `CANCELADO`

## Pagamento

- Apenas Pix, Cartão e Boleto.
- Processamento idempotente se pedido já possui pagamento.

## Avaliação

- Apenas comprador pode avaliar.
- Produto avaliado deve pertencer ao pedido informado.
- Pedido precisa estar entregue.
- Nota entre 1 e 5.
- Mesmo cliente não avalia o mesmo produto duas vezes.

## Cupom

- Código único.
- Vendedor só cria cupom para produto próprio.
- Cupom vinculado a um produto específico.
- Cupom válido apenas se ativo, não expirado, com uso disponível e produto compatível.
- Consumo do cupom ocorre somente após finalização da compra.

---


**Observações:**

- Um cliente pode ter vários pedidos.
- Avaliação é associada a cliente e produto (não diretamente ao pedido).
- Cada pedido possui no máximo um pagamento e uma entrega.

---

## Padrão de Erros

Formato padrão:

```json
{
  "status": 422,
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "Mensagem de erro",
  "details": {},
  "timestamp": "2026-04-27T10:00:00"
}
```

Mapeamento de exceções:

- `NoSuchElementException` -> `404 RESOURCE_NOT_FOUND`
- `IllegalArgumentException` -> `400 BAD_REQUEST`
- `IllegalStateException` -> `422 BUSINESS_RULE_VIOLATION`
- Exceção genérica -> `500 INTERNAL_SERVER_ERROR`

---

## Checklist de Testes Sugeridos

- Login cliente e vendedor
- Fluxo de carrinho completo
- Checkout com endereço obrigatório
- Pedido e pagamento concluídos
- Fluxo vendedor (`PAGO -> ENVIADO -> ENTREGUE`)
- Cancelamento em status permitidos
- Avaliação somente para pedido entregue
- Cupom por produto e consumo após compra
- Build backend:
  - `./mvnw -DskipTests package`

---

## Observações Finais

- API stateless com JWT.
- CORS habilitado para ambiente de desenvolvimento.
- `ddl-auto=update` recomendado apenas para desenvolvimento local.
- Para produção, prefira migrações versionadas (ex.: Flyway/Liquibase).
# Projeto Final - Backend E-commerce

API REST do projeto final de Arquitetura de Dados, desenvolvida com Spring Boot, JWT e MySQL.

## Visão geral

- API para fluxo completo de e-commerce: autenticação, catálogo, carrinho, pedidos, pagamento, entrega, avaliações e cupons.
- Controle de acesso por papel (`CLIENTE` e `VENDEDOR`) com JWT stateless.
- Tratamento padronizado de erros de negócio e validação.

## Stack e dependências

- Java 21
- Spring Boot 4
- Spring Web MVC
- Spring Data JPA
- Spring Security
- JWT (`jjwt`)
- MySQL
- Lombok
- Maven Wrapper (`./mvnw`)

## Estrutura do projeto

- Código da API: `ecommerce/src/main/java/com/ProjetoFinal/ecommerce`
- Configurações: `ecommerce/src/main/resources/application.properties`
- Build Maven: `ecommerce/pom.xml`

## Pré-requisitos

- Java 21 instalado
- MySQL em execução
- Banco e usuário com permissões para leitura/escrita

## Configuração de ambiente

O projeto usa `spring.config.import=optional:file:.env[.properties]`, então as variáveis podem ficar em `ecommerce/.env`.

Exemplo:

```env
DB_URL=jdbc:mysql://localhost:3306/ecommerce?useSSL=false&serverTimezone=UTC
DB_USERNAME=root
DB_PASSWORD=senha
JWT_SECRET=uma-chave-secreta-com-pelo-menos-256-bits
```

Configurações relevantes:

- Porta padrão: `8080`
- `spring.jpa.hibernate.ddl-auto=update`
- `spring.jpa.show-sql=true`

## Como rodar

Na pasta `ecommerce`:

```bash
./mvnw spring-boot:run
```

Build sem testes:

```bash
./mvnw -DskipTests package
```

Compilar:

```bash
./mvnw -DskipTests compile
```

## Autenticação

### Login

- `POST /api/auth/login` (público)
- Body:

```json
{
  "email": "cliente@email.com",
  "senha": "123456"
}
```

- Resposta:

```json
{
  "token": "jwt",
  "tipo": "CLIENTE",
  "id": 1,
  "email": "cliente@email.com",
  "nome": "Cliente"
}
```

### Cadastro

- `POST /api/auth/register/cliente` (público)
- `POST /api/auth/register/vendedor` (público)

### Uso do token

Para rotas protegidas:

```http
Authorization: Bearer <token>
```

## Matriz de autorização (resumo)

### Públicas

- `/api/auth/**`
- `GET /api/produtos/**`
- `GET /api/categorias/**`
- `GET /api/avaliacoes/produto/**`

### Somente cliente (`ROLE_CLIENTE`)

- `/api/clientes/**`
- `/api/carrinho/**`
- `POST /api/pedidos/cliente/**`
- `GET /api/pedidos/cliente/**`
- `/api/pagamentos/**`
- `POST /api/avaliacoes/**`
- `POST /api/cupons/validar`
- `POST /api/cupons/consumir`

### Somente vendedor (`ROLE_VENDEDOR`)

- `/api/vendedores/**`
- `/api/usuarios/**`
- `/api/estoques/**`
- `/api/cupons/**` (exceto validar/consumir)
- `POST|PUT|DELETE /api/produtos/**`
- `GET /api/produtos/vendedor/**`
- `GET /api/pedidos/vendedor/**`
- `PATCH /api/entregas/**`

### Autenticado (qualquer perfil)

- `PATCH /api/pedidos/**`
- `GET /api/entregas/pedido/**`
- Demais rotas não listadas como públicas

## Endpoints

## Auth

- `POST /api/auth/login`
- `POST /api/auth/register/cliente`
- `POST /api/auth/register/vendedor`

## Usuários

- `GET /api/usuarios`
- `GET /api/usuarios/{id}`
- `DELETE /api/usuarios/{id}`

## Clientes

- `POST /api/clientes`
- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `PUT /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

## Vendedores

- `POST /api/vendedores`
- `GET /api/vendedores`
- `GET /api/vendedores/{id}`
- `PUT /api/vendedores/{id}`
- `DELETE /api/vendedores/{id}`

## Produtos

- `POST /api/produtos`
- `GET /api/produtos`
- `GET /api/produtos/{id}`
- `GET /api/produtos/vendedor/{vendedorId}`
- `GET /api/produtos/categoria/{categoriaId}`
- `PUT /api/produtos/{id}`
- `DELETE /api/produtos/{id}`

## Categorias

- `POST /api/categorias`
- `GET /api/categorias`
- `GET /api/categorias/{id}`
- `PUT /api/categorias/{id}`
- `DELETE /api/categorias/{id}`

## Estoques

- `POST /api/estoques`
- `GET /api/estoques`
- `GET /api/estoques/{id}`
- `GET /api/estoques/produto/{produtoId}`
- `PUT /api/estoques/{id}`
- `DELETE /api/estoques/{id}`

## Carrinho

- `GET /api/carrinho/{clienteId}`
- `POST /api/carrinho/{clienteId}/itens?produtoId={id}&quantidade={n}`
- `DELETE /api/carrinho/{clienteId}/itens/{produtoId}`
- `DELETE /api/carrinho/{clienteId}/limpar`

## Pedidos

- `POST /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos`
- `GET /api/pedidos/{id}`
- `GET /api/pedidos/cliente/{clienteId}`
- `GET /api/pedidos/vendedor/{vendedorId}`
- `PATCH /api/pedidos/{id}/status?status={STATUS}`
- `PATCH /api/pedidos/{id}/cancelar`

## Entregas

- `POST /api/entregas/pedido/{pedidoId}`
- `GET /api/entregas`
- `GET /api/entregas/{id}`
- `GET /api/entregas/pedido/{pedidoId}`
- `PATCH /api/entregas/{id}/status?status={STATUS}`
- `DELETE /api/entregas/{id}`

## Pagamentos

- `POST /api/pagamentos/pedido/{pedidoId}`
- `GET /api/pagamentos`
- `GET /api/pagamentos/{id}`
- `DELETE /api/pagamentos/{id}`

Payload do POST:

```json
{
  "tipo": "PagamentoPix"
}
```

Tipos aceitos:

- `PagamentoPix`
- `PagamentoCartao`
- `PagamentoBoleto`

## Avaliações

- `POST /api/avaliacoes?pedidoId={id}`
- `GET /api/avaliacoes/produto/{produtoId}`
- `GET /api/avaliacoes/cliente/{clienteId}`
- `GET /api/avaliacoes/{id}`
- `GET /api/avaliacoes/produto/{produtoId}/media`
- `DELETE /api/avaliacoes/{id}`

## Cupons

- `POST /api/cupons`
- `GET /api/cupons`
- `GET /api/cupons/{id}`
- `GET /api/cupons/vendedor/{vendedorId}`
- `PUT /api/cupons/{id}`
- `DELETE /api/cupons/{id}`
- `POST /api/cupons/validar?codigo={CODIGO}&produtoId={ID}`
- `POST /api/cupons/consumir?codigo={CODIGO}&produtoId={ID}`

## Regras de negócio principais

## Cadastro e autenticação

- Email e CPF são únicos no cadastro.
- CPF é normalizado antes de validar duplicidade.
- Senha é armazenada com BCrypt.

## Produto, categoria e estoque

- Categoria não pode ter nome duplicado.
- Produto precisa estar associado a um vendedor.
- Reserva de estoque falha quando não há quantidade suficiente.

## Carrinho

- Não permite adicionar quantidade menor ou igual a zero.
- Vendedor não pode comprar produto próprio.
- Item só entra no carrinho com estoque disponível.

## Pedido e entrega

- Não cria pedido com carrinho vazio.
- Pedido calcula total pela soma de itens.
- Endereço de entrega é obrigatório para checkout.
- Cancelamento é bloqueado quando pedido já foi enviado/entregue.
- Cancelamento devolve estoque e cancela entrega.

## Pagamento

- Só aceita Pix, Cartão e Boleto.
- Processamento é idempotente quando já existe pagamento no pedido.

## Avaliação

- Só o comprador pode avaliar.
- Só pode avaliar produto que estava no pedido.
- Pedido precisa estar entregue para avaliação.
- Cliente não pode avaliar o mesmo produto mais de uma vez.
- Nota deve estar entre 1 e 5.

## Cupom

- Código de cupom é único.
- Vendedor só cria cupom para produto dele.
- Cupom é vinculado a produto específico.
- Cupom só é válido se ativo, dentro da validade, abaixo do limite de uso e no produto correto.
- Cupom só é consumido após compra finalizada (validação e consumo separados).

## Status de pedido

- `AGUARDANDO_PAGAMENTO`
- `PAGO`
- `EM_PREPARACAO`
- `ENVIADO`
- `ENTREGUE`
- `CANCELADO`

## Relacionamentos entre entidades (JPA)

Abaixo estão os principais relacionamentos do domínio implementados no backend:

- `Cliente 1:N Endereco`
- `Cliente 1:1 Carrinho`
- `Pedido N:1 Cliente`
- `Pedido 1:N ItemPedido`
- `Pedido 1:1 Pagamento`
- `Pedido 1:1 Entrega`
- `ItemPedido N:1 Produto`
- `ItemCarrinho N:1 Carrinho`
- `ItemCarrinho N:1 Produto`
- `Produto N:1 Categoria`
- `Produto N:1 Vendedor`
- `Produto 1:1 Estoque`
- `Produto 1:N Avaliacao`
- `Avaliacao N:1 Cliente`
- `Avaliacao N:1 Produto`
- `Cupom N:1 Produto`
- `Entrega N:1 Endereco`

### Observações de modelagem

- Um cliente pode ter vários pedidos (relação via `Pedido -> Cliente`).
- Avaliação é vinculada a **cliente + produto** (não diretamente ao pedido).
- Cupom é vinculado a um produto específico.
- Cada pedido possui no máximo um pagamento e uma entrega.

## Formato padrão de erro

Exemplo:

```json
{
  "status": 422,
  "code": "BUSINESS_RULE_VIOLATION",
  "message": "Mensagem de erro",
  "details": {},
  "timestamp": "2026-04-27T10:00:00"
}
```

Mapeamento:

- `NoSuchElementException` -> `404 RESOURCE_NOT_FOUND`
- `IllegalArgumentException` -> `400 BAD_REQUEST`
- `IllegalStateException` -> `422 BUSINESS_RULE_VIOLATION`
- `Exception` genérica -> `500 INTERNAL_SERVER_ERROR`

## CORS

- `allowedOriginPatterns=*`
- Métodos permitidos: `GET, POST, PUT, PATCH, DELETE, OPTIONS`
- Headers permitidos: `*`

## Observações importantes

- A API usa sessão stateless com JWT.
- Se o token estiver ausente/inválido em rota protegida, retorna `401 Unauthorized`.
- O projeto está com `ddl-auto=update` em ambiente local.