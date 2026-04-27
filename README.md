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
- `PATCH /api/produtos/{id}/ativo?ativo=true|false`
- `DELETE /api/produtos/{id}` *(soft delete: desativa o produto)*
- Regra: produto inativo nao aparece em listagens publicas para compra.

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
- Produto pode ser ativado/desativado pelo vendedor.
- Excluir produto aplica soft delete (desativacao), mantendo historico de pedidos.
- Produto inativo nao pode ser comprado e nao aparece no catalogo publico.

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
