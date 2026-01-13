# Dockerfile (melhorado) - Java 21, Maven build em stage separado e runtime leve
FROM maven:3.9.5-eclipse-temurin-21 AS build
WORKDIR /workspace
# Copia apenas o POM primeiro para usar cache de dependências
COPY pom.xml ./
RUN mvn -B -DskipTests dependency:go-offline
# Copia o código e compila
COPY src ./src
RUN mvn -B -DskipTests package

# Runtime: imagem leve com JRE 21
FROM eclipse-temurin:21-jre-alpine AS runtime
WORKDIR /app
# Copia o JAR construído (usa wildcard para acomodar nomes gerados pelo Spring Boot)
COPY --from=build /workspace/target/*.jar app.jar
# Cria usuário não-root
RUN addgroup -S app && adduser -S app -G app && chown -R app:app /app
USER app
ENV JAVA_OPTS=""
EXPOSE 8080
ENTRYPOINT ["sh","-c","exec java $JAVA_OPTS -jar /app/app.jar"]