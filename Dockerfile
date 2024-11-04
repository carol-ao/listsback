# This is a multi-stage Dockerfile

# Image with the required files to build and test the application.
# It will be used to build the final image and will be discarded after that.
# referenced in this file as 'build'
FROM maven:3.9.9-amazoncorretto-21 AS build

# Determina a pasta atual de trabalho: /app
WORKDIR /app
# Copia todos os arquivos que estão na pasta um nível acima (..) para a pasta atual da imagem (. -> workdir -> /app)
COPY . .

#RUN mvn test -Dspring.profiles.active=test
RUN mvn clean install -DskipTests

# Another image, this one will be the final one, smaller, with only the necessary files
FROM eclipse-temurin:21

# Determina a pasta atual de trabalho: /app
WORKDIR /app
# Copy the JAR file from the build image all files in /app/target/ that ends with '.jar' to the current folder (WORKDIR)
# and rename it to app.jar
COPY --from=build /app/target/*.jar app.jar
# Deixa essa porta do contêiner acessível
EXPOSE 8080

# Executa o comando `java -jar app.jar` dentro da pasta de trabalho quando o contêiner for iniciado
CMD ["java", "-jar", "app.jar"]
