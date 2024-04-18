FROM maven:3.6.3-openjdk-11 AS builder

# La variable d'environnement READPASSWORD est passée en argument lors du build : docker build -t ... --build-arg READPASSWORD=...
# Elle est utilisée par substitution dans /root/.m2/settings.xml (<password>${env.READPASSWORD}</password>)
ARG READPASSWORD

RUN apt-get update && apt-get -y install dos2unix

WORKDIR /opt/build/
COPY .secrets/settings-read.xml /root/.m2/settings.xml
COPY pom.xml .
RUN mount=type=cache,target=/root/.m2 mvn -e -B dependency:resolve
COPY src src
RUN mvn -e -B -Dmaven.test.skip=true package


FROM openjdk:11-jdk-slim
ENV DEBIAN_FRONTEND=noninteractive

RUN apt update -y && apt install -y dos2unix curl netcat procps dnsutils
RUN apt -yqq install krb5-user libpam-krb5

COPY docker/krb5.conf /etc/krb5.conf
COPY docker/login.conf /app/login.conf
COPY docker/sharepoint.keytab /app/.
COPY docker/sharepoint.keytab /etc/krb5.keytab
COPY docker/logback.xml /app/.

ENV RESTPROXY_JAR="sharepointrestproxy-0.1.jar"
#ENV user
#ENV passwd

COPY --from=builder /opt/build/target/${RESTPROXY_JAR} /app/.
#COPY target/${RESTPROXY_JAR} /app/.

ENV RESTPROXY_CONFIG=config.yml
EXPOSE 9990

WORKDIR /app
COPY docker/entrypoint.sh config*.yml /app/.
COPY docker/config*.yml /app/.
RUN cd /app && dos2unix *.sh *.yml

ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh" ]
