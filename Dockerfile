# Build stage
FROM maven:3.6.3-openjdk-11 AS build
ENV SQL_URL jdbc:mysql://localhost:3306/acme_corp_developer_iq
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app
RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Package stage
FROM openjdk:11
COPY --from=build /usr/src/app/target/developeriq-0.0.1-SNAPSHOT.jar /usr/local/lib/developeriq.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/usr/local/lib/developeriq.jar"]