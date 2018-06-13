# builds a docker image of this spring boot application
# to run the docker image:
#  docker run -p 8080:8080 -t strohs/spring-boot-blogen
FROM openjdk:8-jdk-alpine
VOLUME /tmp
ARG JAR_FILE
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]