FROM openjdk:8-jdk-alpine

LABEL maintainer="apatronov@i-novus.ru"

RUN apk add tzdata
ENV TZ=Europe/Moscow

ENV APP_HOME=./config
ENV CXF_PATH=/integration/ws
ARG JAR_FILE
RUN mkdir config
RUN mkdir share
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","app.jar"]
