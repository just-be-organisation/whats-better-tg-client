FROM adoptopenjdk/openjdk11

MAINTAINER Vladyslav Yemelianov <vlad.yem.dev@gmail.com>

ADD ./target/tg-client.jar /app/
CMD ["java", "-Xmx512m", "-jar", "/app/tg-client.jar"]

EXPOSE 8080