FROM grpc-web-demo_base

ARG BASEDIR=/usr/src/base
ARG PROTO_PATH=${BASEDIR}/helloworld.proto

RUN mkdir -p ${BASEDIR}

RUN cp /tmp/helloworld.proto ${BASEDIR}/helloworld.proto

WORKDIR ${BASEDIR}

COPY client.js .
COPY package.json .


RUN protoc -I=. helloworld.proto --js_out=import_style=commonjs:. --grpc-web_out=import_style=commonjs,mode=grpcwebtext:.

RUN npm install
RUN npx webpack client.js

CMD echo "I am base javascript image, bye bye"

