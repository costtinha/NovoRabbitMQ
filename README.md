# 🛍️ Sistema de Pedidos e Pagamentos com Mensageria Assíncrona (RabbitMQ)

Projeto multi-módulo **Spring Boot** demonstrando arquitetura event-driven: um serviço de **ordens** produz eventos e outro serviço de **pagamentos** consome e processa essas mensagens de forma assíncrona.

Foco em:
- Separação clara de responsabilidades
- Comunicação assíncrona via RabbitMQ (JSON)
- Cache com Redis no serviço de ordens
- Configuração centralizada via módulo **pom pai**

---

## 📋 Índice

- [Sobre o projeto](#-sobre-o-projeto)
- [Por que mensageria assíncrona?](#-por-que-mensageria-assíncrona)
- [Arquitetura](#-arquitetura)
- [Tecnologias](#-tecnologias)
- [Pré-requisitos](#-pré-requisitos)
- [Instalação e Execução](#-instalação-e-execução)
- [Endpoints Principais](#-endpoints-principais)
- [Fluxo de Mensagens](#-fluxo-de-mensagens)
- [Estrutura do Projeto](#-estrutura-do-projeto)
- [Decisões & Aprendizados](#-decisões--aprendizados)
- [Licença](#-licença)

---

## 🚀 Sobre o projeto

API simples para criação e consulta de **pedidos** (order-service) que, ao criar um pedido, publica um evento JSON no RabbitMQ.  
O **payment-service** consome esse evento de forma assíncrona e realiza (neste exemplo) apenas o log da mensagem recebida — pronto para ser estendido com regras de pagamento, integração com gateway, atualização de status, etc.

Ideal para portfólio, estudo de mensageria ou como base para sistemas reais de e-commerce / ERP.

---

## ⚡ Por que mensageria assíncrona?

| Abordagem               | Acoplamento | Performance | Resiliência | Escalabilidade |
|-------------------------|-------------|-------------|-------------|----------------|
| Chamada HTTP síncrona   | Alto        | Bloqueante  | Baixa       | Limitada       |
| Mensageria (RabbitMQ)   | Baixo       | Assíncrona  | Alta        | Excelente      |

Benefícios neste projeto:
- Desacoplamento total entre criação de pedido e processamento de pagamento
- Tolerância a falhas (pagamento pode falhar sem derrubar o pedido)
- Escalabilidade independente dos serviços

---

## 🏗️ Arquitetura

HTTP POST /order-service
│
▼
┌──────────────────┐
│  order-service   │
│  (Producer)      │
└─────────┬────────┘
│  publica OrderResponseDTO (JSON)
▼
┌───────────────┐
│   RabbitMQ    │
│  exchange     │
│  order_queue  │
└───────┬───────┘
▼  consome
┌────────────────────┐
│  payment-service   │
│  (Consumer)        │
└────────────────────┘


---

## 🛠 Tecnologias

| Tecnologia              | Versão       | Finalidade principal                          |
|-------------------------|--------------|-----------------------------------------------|
| Java                    | 21           | Linguagem                                     |
| Spring Boot             | 3.4.3        | Framework principal                           |
| Spring AMQP             | —            | Integração com RabbitMQ                       |
| Spring Data JPA         | —            | Persistência PostgreSQL (order-service)       |
| Spring Data Redis       | —            | Cache de leitura (order-service)              |
| PostgreSQL              | —            | Banco de dados principal                      |
| Redis                   | —            | Cache por pedido                              |
| Maven                   | 3.9+         | Gerenciamento multi-módulo                    |
| Docker (opcional)       | —            | RabbitMQ + PostgreSQL + Redis                 |

---

## 📦 Pré-requisitos

- Java 21+
- Maven 3.9+
- Docker + Docker Compose (recomendado para RabbitMQ, PostgreSQL e Redis)

---

## 🚀 Instalação e Execução

### 1. Com Docker Compose (mais fácil)

```bash
# 1. Clone o repositório
git clone https://github.com/costtinha/NovoRabbitMQ
cd NovoRabbitMQ
```
# 2. Subir dependências (RabbitMQ + PostgreSQL + Redis)
```bash
docker compose up -d
```

# 3. Compilar e rodar os serviços
```bash
mvn clean install
```

# 4. Depois execute cada módulo em terminais separados:
# Terminal 1 - Order Service
cd order-service
mvn spring-boot:run

# Terminal 2 - Payment Service
cd payment-service
mvn spring-boot:run

A API estará disponível em:

Order Service → http://localhost:8080/order-service
RabbitMQ Management → http://localhost:15672 (guest/guest)
--- 
# Endpoints Principais (order-service)
Base URL: `http://localhost:8080/order-service`

| Método | Endpoint                        | Descrição                                      | Body / Parâmetros esperados                          | Retorno principal                          |
|--------|---------------------------------|------------------------------------------------|-------------------------------------------------------|--------------------------------------------|
| `GET`  | `/`                             | Lista todos os pedidos cadastrados             | —                                                     | Lista de `OrderResponseDTO`                |
| `POST` | `/`                             | Cria um novo pedido e publica evento no RabbitMQ | JSON: `{ "customerName": "...", "totalAmount": 299.90, "items": ["..."] }` | `OrderResponseDTO` do pedido criado       |
| `GET`  | `/{order-id}`                   | Busca um pedido por ID (cache Redis → DB)      | Path: `order-id` (ex: 1)                              | `OrderResponseDTO` ou null/404             |
| `DELETE`| `/{order-id}`                  | Remove um pedido (banco + cache)               | Path: `order-id` (ex: 5)                              | 200 OK (sem corpo) ou 404                  |
---
### Exemplo de criação
```bash
curl -X POST http://localhost:8080/order-service \
-H "Content-Type: application/json" \
-d '{
"customerName": "Maria Silva",
"totalAmount": 299.90,
"items": ["Café", "Bolo"]
}'
```

# Fluxo de Mensagens

- Cliente → POST /order-service
- OrderService salva no PostgreSQL + Redis
- OrderProducer publica OrderResponseDTO no exchange exchange / routing key order_key
- RabbitMQ entrega mensagem na queue order_queue
- OrderConsumer (payment-service) recebe e loga a mensagem (pronto para implementar pagamento)

# Estrutura do projeto
```
order-payment/                  ← módulo pai (pom)
├── pom.xml                     ← dependências e plugins comuns
├── order-service/
│   ├── pom.xml
│   ├── src/main/java/com/example/order_service/
│   │   ├── config/             ← RabbitMQ + Redis + JPA
│   │   ├── controller/
│   │   ├── service/            ← OrderService + Producer
│   │   ├── producer/
│   │   ├── cache/              ← Redis repository
│   │   ├── persistance/        ← JPA repository
│   │   └── entity/
│   └── src/main/resources/
└── payment-service/
├── pom.xml
├── src/main/java/com/example/payment_service/
│   ├── config/             ← RabbitMQ consumer config
│   └── consumer/           ← OrderConsumer
└── src/main/resources/
```

# Decisões & Aprendizados

- Módulo pai com dependencyManagement + spring-boot-dependencies import
- Uso de Jackson2JsonMessageConverter para serialização limpa de DTOs
- Cache de leitura com Redis (first read → cache, miss → DB + cache)
- Fila durável (true) para evitar perda de mensagens em restart do broker
- Producer e Consumer em projetos separados → simula microsserviços reais
---

## 📄 Licença

Este projeto é de uso educacional e está disponível sob a licença [MIT](LICENSE).

---

<p align="center">
  Desenvolvido por <a href="https://github.com/costtinha">Daniel Costa</a>
</p>
