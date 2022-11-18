FROM openjdk:17-oracle

VOLUME /tmp
ARG JAR_FILE
ADD build/libs/takehome-0.0.1-SNAPSHOT.jar /app/app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]