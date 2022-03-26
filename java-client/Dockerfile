FROM grpc-web-demo_server1


ARG PORT=50051
ARG HOSTNAME=localhost
ARG USE_PLAINTEXT=true


ENV PORT=${PORT}
ENV HOSTNAME=${HOSTNAME}
ENV USE_PLAINTEXT=${USE_PLAINTEXT}

CMD mvn exec:java -Dexec.mainClass=com.studiozdev.grpc.webdemo.GreeterClient
