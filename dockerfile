# Diz de qual imagem tudo vai partir. Nomeia essa imagem como 'build'
FROM maven:3.9.9-amazoncorretto-21 AS build

# Determina a pasta atual de trabalho: /app
WORKDIR /app
# Copia todos os arquivos que estão na pasta um nível acima (..) para a pasta atual da imagem (. -> workdir -> /app)
COPY . .

#RUN mvn test -Dspring.profiles.active=test
RUN mvn clean package -DskipTests

# Outra imagem, essa se tornará um contêiner
FROM eclipse-temurin:21

# Determina a pasta atual de trabalho: /app
WORKDIR /app
# Copia da imagem chamada 'build', tudos os arquivos que estiverem em /app/target/ que terminem com .jar para a pasta atual da imagem
COPY --from=build /app/target/*.jar app.jar
# Deixa essa porta do contêiner acessível
EXPOSE 8080

# Executa o comando `java -jar app.jar` dentro da pasta de trabalho quando o contêiner for iniciado
CMD ["java", "-jar", "app.jar"]
