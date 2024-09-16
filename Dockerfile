#========================================
FROM maven:3.9-eclipse-temurin-17 AS builder

# La variable d'environnement READPASSWORD est passée en argument lors du build : docker build -t ... --build-arg READPASSWORD=...
# Elle est utilisée par substitution dans /root/.m2/settings.xml (<password>${env.READPASSWORD}</password>)
ARG READPASSWORD

WORKDIR /opt/build/
COPY .secrets/settings-read.xml /root/.m2/settings.xml
COPY pom.xml .
RUN mount=type=cache,target=/root/.m2 mvn -e -B dependency:resolve
COPY src src
RUN mvn -e -B -Dmaven.test.skip=true package

#========================================
FROM docker.pkg.github.com/taligentia/cea/kamare_base:1.2.0
ENV DEBIAN_FRONTEND=noninteractive

ENV RESTPROXY_JAR="sharepointrestproxy-0.1.7.jar"
ENV RESTPROXY_CONFIG=config.yml

WORKDIR /app

COPY dockerimage/* /app/.
COPY dockerimage/templates /app/templates
COPY --from=builder /opt/build/target/${RESTPROXY_JAR} /app/.
RUN cd /app && dos2unix *.sh *.yml && chmod +x *.sh

EXPOSE 9990

ENTRYPOINT ["/bin/bash", "/app/entrypoint.sh" ]