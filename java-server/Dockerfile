FROM grpc-web-demo_base as PROTO_BASE




FROM maven:3.6.3-jdk-8

ARG WORKDIR=/usr/src/java-server
ARG BASEDIR=/usr/src/base
ARG PROTO_PATH=${BASEDIR}/helloworld.proto
ARG PORT=50001

RUN mkdir -p ${WORKDIR}
RUN mkdir -p ${BASEDIR}

COPY --from=PROTO_BASE /tmp/helloworld.proto ${BASEDIR}/helloworld.proto

WORKDIR /${WORKDIR}

COPY ./pom.xml pom.xml
RUN mvn exec:help
RUN mvn validate

ENV PORT=${PORT}
ENV HOSTNAME=${HOSTNAME}
EXPOSE ${PORT}

COPY . .
RUN mvn verify

CMD mvn exec:java -Dexec.mainClass=com.studiozdev.grpc.webdemo.HostnameServer
