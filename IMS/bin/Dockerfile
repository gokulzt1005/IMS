FROM maven:3.6.0-jdk-8-slim AS build
COPY . .
RUN mvn clean package -DskiptTests
FROM openjdk:8-jre-slim
COPY --from=build /target/IMS-0.0.1-SNAPSHOT.jar /usr/local/lib/IMS.jar
ENV PORT 8080
#EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/IMS.jar"]
