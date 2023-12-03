# Build stage
#FROM maven:3.8.5-openjdk-17 AS build
#ENV SQL_URL jdbc:mysql://localhost:3306/acme_corp_developer_iq
#ENV PAT_TOKEN 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
#COPY src /usr/src/app/src
#COPY pom.xml /usr/src/app
#RUN mvn -f /usr/src/app/pom.xml clean package -DskipTests

# Package stage
#FROM openjdk:17
#COPY --from=build /usr/src/app/target/developeriq-0.0.1-SNAPSHOT.jar /usr/local/lib/developeriq.jar
#EXPOSE 8080
#ENTRYPOINT ["java","-jar","/usr/local/lib/developeriq.jar"]

# Build instructions for CMM707_Docker_Server EC2 Instance
FROM openjdk:17 as build
ENV SQL_USER dbeaver
ENV SQL_PASSWORD dbeaver
ENV SQL_URL jdbc:mysql://localhost:3306/acme_corp_developer_iq
ENV PAT_TOKEN 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
COPY developeriq-0.0.1-SNAPSHOT.jar developeriq.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","developeriq.jar"]