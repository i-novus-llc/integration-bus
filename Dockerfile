FROM openjdk:8-jre-alpine

LABEL maintainer="apatronov@i-novus.ru"

ENV APP_HOME=./config
ARG JAR_FILE
RUN mkdir config
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
