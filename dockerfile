FROM eclipse-temurin:17-jre-alpine

WORKDIR /app

# target下的jar复制到应用目录
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} app.jar

ENV SPRING_PROFILES_ACTIVE=prod

ENTRYPOINT ["sh", "-c", "java -jar /app/app.jar"]