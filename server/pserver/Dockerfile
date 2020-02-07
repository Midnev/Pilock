FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD /src/main/resources/*.json src/main/resources/pilock-firebase-adminsdk.json
ADD /target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]