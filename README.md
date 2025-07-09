# 📚 Bookstore API - Projeto de Estudo com Spring Boot 3.3.1

**Autor:** Lenin Otero  
**GitHub:** https://github.com/leninotero

Este é um projeto de estudo que consiste na criação de uma API REST para uma livraria (Bookstore), desenvolvido utilizando a linguagem Java 21, o framework Spring Boot 3.3.1, documentação com Springdoc OpenAPI, e persistência de dados com o banco de dados PostgreSQL.

## 🚀 Tecnologias Utilizadas

- **Java 21**
- **Spring Boot 3.3.1**
- **Spring Web**
- **Spring Data JPA**
- **PostgreSQL**
- **Springdoc OpenAPI (Swagger UI)**
- **Maven**
- **Docker**

## 📘 Sobre o Spring Boot

Spring Boot é um framework baseado no Spring que facilita a criação de aplicações Java autônomas, com configuração mínima e práticas recomendadas embutidas. Ele permite que você crie APIs RESTful de maneira simples e rápida, abstraindo diversas configurações complexas.

## 🎯 Objetivo do Projeto

Este projeto foi desenvolvido com o objetivo de praticar os conceitos de desenvolvimento de APIs RESTful utilizando Java moderno e ferramentas populares do ecossistema Spring. Ele simula uma livraria com funcionalidades básicas de cadastro, listagem, atualização e remoção de livros.

## 🔧 Funcionalidades da API

- ✅ Cadastrar novo livro
- 📖 Listar todos os livros
- 🔍 Buscar livro por ID
- ✏️ Atualizar informações de um livro
- ❌ Deletar livro por ID

## 📂 Estrutura do Projeto
```
src
├── main
│ ├── java
│ │ └── com.example.bookstore
│ │ ├── controller
│ │ ├── model
│ │ ├── repository
│ │ ├── service
│ │ └── BookstoreApplication.java
│ └── resources
│ ├── application.properties
```

## 🔗 Documentação da API
Após executar o projeto, a documentação estará disponível em:

```bat
http://localhost:8080/swagger-ui.html
```

## 🛠️ Configuração do Banco de Dados
O projeto utiliza PostgreSQL. Configure o application.properties da seguinte forma:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

springdoc.api-docs.path=/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
```

## ▶️ Como Executar o Projeto
1. Clone o repositório:
```bat
git clone https://github.com/seu-usuario/bookstore-api.git
```
2. Configure o PostgreSQL e crie o banco bookstore.
3. Altere as credenciais no application.properties.
4. Execute o projeto:
```bat
./mvnw spring-boot:run
```

## Docker
Execute o seguinte comando para buildar a imagem no docker:
```bat
docker build -t lojalivros .
```
Execute o seguinte comando para dar startar a imagem
```bat
docker run -p 8080:8080 lojalivros
```
Execute este comando para executar o docker compose com as configurações do postgreSQL
```bat
 docker-compose up -d
```

## 🧠 Conclusão
- Este projeto serve como uma base para aprender sobre:
- Criação de APIs REST com Spring Boot
- Integração com banco de dados usando Spring Data JPA
- Documentação automática com OpenAPI/Swagger
- Boas práticas no desenvolvimento de aplicações Java modernas

Desenvolvido com fins educacionais por lenin abadie.
