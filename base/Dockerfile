FROM ubuntu:20.04 AS protobuf

RUN apt-get -qq update && apt-get -qq install -y curl unzip

WORKDIR /tmp

ARG PROTOC_GEN_GRPC_WEB=1.3.1
ARG PROTOBUF_VERSION=3.19.4

RUN curl -sSL https://github.com/protocolbuffers/protobuf/releases/download/v$PROTOBUF_VERSION/\
protoc-$PROTOBUF_VERSION-linux-x86_64.zip -o protoc.zip && \
  unzip -qq protoc.zip && \
  cp ./bin/protoc /usr/local/bin/protoc

RUN apt install -y nodejs && apt install -y npm

RUN curl -sSL https://github.com/grpc/grpc-web/releases/download/${PROTOC_GEN_GRPC_WEB}/\
protoc-gen-grpc-web-${PROTOC_GEN_GRPC_WEB}-linux-x86_64 -o protoc-gen-grpc-web
RUN cp ./protoc-gen-grpc-web /usr/local/bin/protoc-gen-grpc-web && chmod +x /usr/local/bin/protoc-gen-grpc-web

COPY helloworld.proto .

CMD echo "I am base image, bye bye"

