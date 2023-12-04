# Build instructions for CMM707_Ansible_Server EC2 Instance
# Jenkins will run this Dockerfile using the ansible-playbook
FROM openjdk:17 as build
ENV SQL_USER dbeaver
ENV SQL_PASSWORD dbeaver
ENV SQL_URL jdbc:mysql://localhost:3306/acme_corp_developer_iq
ENV PAT_TOKEN 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
COPY developeriq-0.0.1-SNAPSHOT.jar developeriq.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","developeriq.jar"]