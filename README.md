# Coupon API 🎟️

![Java](https://img.shields.io/badge/Java-21%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-6DB33F?style=for-the-badge&logo=spring-boot&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-2496ED?style=for-the-badge&logo=docker&logoColor=white)

## 📖 Sobre o Projeto

A **Coupon API** é uma interface RESTful desenvolvida para gerenciar o ciclo de vida de cupons de desconto.

Este projeto foca em boas práticas de desenvolvimento backend, incluindo validações robustas e arquitetura limpa.

## 🚀 Tecnologias Utilizadas

* **Linguagem:** Java 21 (ou superior)
* **Framework:** Spring Boot 3
* **Banco de Dados:** H2 DataBase
* **Gerenciamento de Dependências:** Maven
* **Containerização:** Docker & Docker Compose

## ✨ Funcionalidades

* ✅ **Criação de Cupons:** Cadastro de novos cupons com código, percentual de desconto e data de validade.
* 🔍 **Consulta de Cupons:** Busca de cupom por id.
* ⏳ **Remoção de Cupons:** Remoção inteligente de cupons, mantendo informações salvar no banco.

## 🛠️ Como Executar o Projeto

### Pré-requisitos

Certifique-se de ter instalado em sua máquina:
* [Java JDK 21+](https://www.oracle.com/java/technologies/downloads/)
* [Maven](https://maven.apache.org/)
* [Docker](https://www.docker.com/)

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/Joao-Darwin/coupon-api.git](https://github.com/Joao-Darwin/coupon-api.git)
    cd coupon-api
    ```

2.  **Usando docker:**
    Se estiver usando Docker, basta subir a aplicação:
    ```bash
    docker-compose up -d
    ```
    A aplicação estará rodando em `http://localhost:8080`.

3.  **Compile e Execute:**
    ```bash
    mvn spring-boot:run
    ```

4.  **Acesse a API:**
    A aplicação estará rodando em `http://localhost:8080`.

## 🔌 Documentação da API (Exemplos)

Aqui estão alguns exemplos de requisições que você pode testar via Postman ou Insomnia:

### 1. Criar um Cupom (`POST /api/v1/coupons`)
**Body (JSON):**
```json
{
  "code": "ABC-123",
  "description": "Iure saepe amet. Excepturi saepe inventore nam doloremque voluptatem a. Quaerat odio distinctio eos. Dolor debitis ex molestias nam quae hic suscipit odit nulla. Blanditiis ratione facilis nobis quam deserunt. Doloribus iste corrupti magni ipsum illo beatae consectetur.",
  "discountValue": 0.8,
  "expirationDate": "2025-11-04T17:14:45.180Z",
  "published": false
}
````
### 2. Obter um cupom (`GET /api/v1/coupons/{id}`)
**Response (JSON):**
```json
{
  "id": "598c5a85-46d6-4c69-8513-6ecfb9b5d7e2",
  "code": "ABC123",
  "description": "At dolore nobis aut veniam ex sapiente. Rem libero ratione dolore eaque perspiciatis possimus. Quisquam adipisci nulla aperiam iure explicabo. Cumque id asperiores. At dignissimos eaque saepe maiores maiores.",
  "discountValue": 0.8,
  "expirationDate": "2025-11-04T17:40:14.609Z",
  "status": "ACTIVE",
  "published": true,
  "redeemed": false
}
````
### 3. Deletar um cupom (Soft Delete) (`DELETE /api/v1/coupons/{id}`)

## 👨‍💻 Autor

Joao Darwin
