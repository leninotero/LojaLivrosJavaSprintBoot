# 📚 Loja Livros — Spring Boot API
**Autor:** Lenin Otero  
**GitHub:** https://github.com/leninotero

A RESTful API for a bookstore (loja de livros) built with Spring Boot. It includes CRUD resources for books, authors, publishers and users, with authentication using Spring Security and JWT. OpenAPI/Swagger UI is included for API exploration.

Current local date/time: 2025-11-07 14:55

## 📘 Overview
- Language/Runtime: Java (Java 17 per `pom.xml`)
- Framework: Spring Boot 3.3.x (Web, Data JPA, Security)
- Database: PostgreSQL
- Build/Package Manager: Maven (Maven Wrapper `mvnw`/`mvnw.cmd`)
- API Docs: springdoc-openapi-starter (Swagger UI)
- Auth: Spring Security + JWT (`com.auth0:java-jwt`)
- Containerization: Docker + Docker Compose
- Entry Point: `com.loja_livros.lojalivros.LojalivrosApplication`

Controllers present:
- `AuthorController`, `BookController`, `PublisherController`, `UserController` (auth: register/login)

> NOTE: The Dockerfile uses Temurin 21 while `pom.xml` targets Java 17. See the TODOs below to reconcile JVM versions.

## Requirements
- Java 17 JDK (to match `pom.xml`). If building with Dockerfile as-is, Java 21 runtime will be used.
- Maven Wrapper (included) or Maven 3.9+
- PostgreSQL 14+ (if running locally without Docker)
- Docker & Docker Compose (optional, for containerized run)

## 🚀 Getting Started

### 1) Clone
```
git clone <your-fork-or-repo-url>
cd lojalivros
```

### 2) Configure environment
Application properties defaults (see `src/main/resources/application.properties`):
```
spring.datasource.url=jdbc:postgresql://localhost:5432/bookstore
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
jwt.tocken.secret=-my-secret-key-api
```

Environment variables you can set instead (examples):
- `SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/bookstore`
- `SPRING_DATASOURCE_USERNAME=postgres`
- `SPRING_DATASOURCE_PASSWORD=postgres`
- `SERVER_PORT=8080`
- `SPRING_PROFILES_ACTIVE=dev` (if profiles are added later)
- `JWT_TOCKEN_SECRET=...` (or set `jwt.tocken.secret` via Spring config mechanisms)

> IMPORTANT: The property name is spelled `jwt.tocken.secret` in code (note: "tocken"), used by `TokenService`. Keep consistent or correct in code. See TODOs.

### 3) Run locally (no Docker)
- With Maven Wrapper (Windows):
```
.mvnw.cmd spring-boot:run
```
- With Maven Wrapper (Unix/macOS):
```
./mvnw spring-boot:run
```
The app starts on `http://localhost:8080` by default.

Build a jar:
- Windows:
```
.mvnw.cmd clean package
```
- Unix/macOS:
```
./mvnw clean package
```
Then run (jar name produced by Spring Boot plugin under `target/`):
```
java -jar target/<artifact-name>.jar
```

### 4) Run with Docker Compose
This repo contains a `docker-compose.yml` that builds the app image and starts a PostgreSQL service.

- Build and start in the background:
```
docker compose up --build -d
```
- View logs:
```
docker compose logs -f
```
- Stop:
```
docker compose down
```

Compose details:
- App service: exposes `8080:8080`, depends on Postgres
- Postgres service: `5432:5432`, database `bookstore`, user/password `postgres`
- App environment in Compose sets `SPRING_DATASOURCE_*` pointing to `postgres` service

### 5) API Docs (Swagger UI)
With `springdoc-openapi-starter-webmvc-ui` included, Swagger UI is typically available at:
- `http://localhost:8080/swagger-ui.html` (redirects)
- or `http://localhost:8080/swagger-ui/index.html`

If you customized Springdoc paths, adjust accordingly.

## Authentication
The `UserController` exposes:
- `POST /api/bookstore/auth/register` — registers a user and returns a JWT
- `POST /api/bookstore/auth/login` — authenticates and returns a JWT

JWT handling is implemented in `services/infra/security/TokenService`. The secret is read from the property `jwt.tocken.secret`.

Include the token in requests to secured endpoints, typically via:
```
Authorization: Bearer <token>
```

##  ▶️ Scripts & Tooling
- Maven Wrapper: `mvnw` (Unix) / `mvnw.cmd` (Windows)
    - Run app: `mvnw spring-boot:run`
    - Build jar: `mvnw clean package`
    - Tests: `mvnw test`
- Docker
    - Build multi-stage image via `Dockerfile`
    - Orchestration via `docker-compose.yml`

## Environment Variables
Common variables:
- `SPRING_DATASOURCE_URL` — JDBC URL (Compose uses `jdbc:postgresql://postgres:5432/bookstore`)
- `SPRING_DATASOURCE_USERNAME` — database user
- `SPRING_DATASOURCE_PASSWORD` — database password
- `SERVER_PORT` — override server port (defaults to 8080)
- `SPRING_PROFILES_ACTIVE` — Spring profile (none defined by default)
- `jwt.tocken.secret` — JWT secret (set via properties or env; see note below)

How to set `jwt.tocken.secret` via environment:
- Spring allows relaxed binding for env vars. For a custom property like this, you can export it as:
    - Unix/macOS: `export JWT_TOCKEN_SECRET=your-secret` (and map it in config as needed),
    - or set it via JVM arg: `-Djwt.tocken.secret=your-secret` when launching Java or Maven.

> TIP: In Docker Compose, consider adding an environment entry for the JWT secret in the `app` service.

## Testing
Run unit tests:
- Windows: `.\mvnw.cmd test`
- Unix/macOS: `./mvnw test`

Sample test class: `src/test/java/com/loja_livros/lojalivros/LojalivrosApplicationTests.java`

## 📂 Project Structure
```
lojalivros/
├─ Dockerfile
├─ docker-compose.yml
├─ pom.xml
├─ mvnw, mvnw.cmd
├─ src/
│  ├─ main/java/com/loja_livros/lojalivros/
│  │  ├─ LojalivrosApplication.java              # Spring Boot entry point
│  │  ├─ controllers/                            # REST controllers
│  │  ├─ dtos/                                   # DTO records
│  │  ├─ models/                                 # JPA entities
│  │  ├─ repositories/                           # Spring Data repositories
│  │  └─ services/infra/security/                # Security (JWT, filters, config)
│  └─ main/resources/
│     └─ application.properties
└─ src/test/java/com/loja_livros/lojalivros/
   └─ LojalivrosApplicationTests.java
```

## 🔧 Configuration
Key application properties (see `application.properties`):
- JPA/Hibernate:
    - `spring.jpa.hibernate.ddl-auto=update`
    - `spring.jpa.show-sql=true`
    - `spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect`
- SQL init is disabled: `spring.sql.init.mode=never`

## License
TODO: Add license (e.g., MIT, Apache-2.0). If unsure, consult repository owner and include a `LICENSE` file.

## Roadmap / TODOs
- Reconcile Java versions: Dockerfile uses Java 21, `pom.xml` targets Java 17. Decide on a single version and align Dockerfile and `pom.xml`.
- Property name `jwt.tocken.secret` is likely a typo for `jwt.token.secret`. Either fix the code to `jwt.token.secret` or keep the current name consistently across configs and environment.
- Add Swagger/OpenAPI metadata (title, description) and verify Swagger UI path.
- Document secured endpoints and roles once finalized.
- Add database migration tool (Flyway/Liquibase) if needed.
- Add CI workflow and code coverage reports.

## 🔗 Useful Links
- Spring Boot Maven Plugin: https://docs.spring.io/spring-boot/docs/current/maven-plugin
- Springdoc OpenAPI: https://springdoc.org
- Spring Security: https://spring.io/projects/spring-security
- PostgreSQL: https://www.postgresql.org
