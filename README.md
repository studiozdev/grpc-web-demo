# grpc-web-demo
### Example project to demonstrate grpc server architecture using Nginx+Envoy as reverse proxies
This demo project is for demonstrating how to run grpc server with Nginx+Envoy in docker-compose together with browser and regular clients

###Preparation for this demo
To make this demo work properly, besides installing Docker and docker-compose, we need to have 2 more crucial things:
* Edit the file 'c:\Windows\System32\drivers\etc\hosts' on Windows or '/etc/hosts' on Linux with following line
    * 127.0.0.1 grpc.studiozdev.com
    * This will simulate our local computer as serving this domain
* Get/generate a real authorized HTTPS certificates for the domain "grpc.studiozdev.com" and generate a selfsigned certificate for localhost
    * After being generated or fetched, it should be places under the directory "nginx/ssl" in 'certs' and 'private' directories for public and private certificates respectively

###build and run
just run:
```bash
docker-compose up --build -d
```
After to wath th logs continuously, run
```bash
docker-compose logs -f
```

###Components
![](architecture.png)

Since at the moments, browsers does not support GRPC, we must use the Envoy proxy for that:
![](https://grpc.io/img/grpc-web-proxy.png)



###grpcurl commands
####grpc.studiozdev.com

Call server with plaintext (NO HTTPS)
```bash
grpcurl  -plaintext -d '{"name": "103"}' grpc.studiozdev.com:50051 helloworld.Greeter/SayHello
```
Call server with encryption
```bash
grpcurl  -d '{"name": "103"}' grpc.studiozdev.com:443 helloworld.Greeter/SayHello
```

####localhost with self signed certificate

Call server with plaintext (NO HTTPS)
```bash
grpcurl  -plaintext -d '{"name": "103"}' localhost:50051 helloworld.Greeter/SayHello
```
Call server with encryption
```bash
grpcurl  -d '{"name": "103"}' localhost:443 helloworld.Greeter/SayHello
#output:
Failed to dial target host "localhost:443": x509: certificate relies on legacy Common Name field, use SANs or temporarily enable Common Name matching with GODEBUG=x509ignoreCN=0
#The following works with -insecure flag:
grpcurl -insecure  -d '{"name": "103"}' localhost:443 helloworld.Greeter/SayHello
```

###java client commands
####grpc.studiozdev.com

Call server with plaintext (NO HTTPS)
```bash
docker-compose run -e USE_PLAINTEXT=true -e PORT=50051 -e HOSTNAME=grpc.studiozdev.com client  
```
Call server with encryption
```bash
docker-compose run -e USE_PLAINTEXT=false -e PORT=443 -e HOSTNAME=grpc.studiozdev.com client
```

####localhost with self signed certificate

Call server with plaintext (NO HTTPS)
```bash
docker-compose run -e USE_PLAINTEXT=true -e PORT=50051 -e HOSTNAME=localhost client
```
Call server with encryption
```bash
docker-compose run -e USE_PLAINTEXT=false -e PORT=443 -e HOSTNAME=localhost client
#output:
WARNING: RPC failed: Status{code=UNAVAILABLE, description=io exception, cause=io.netty.channel.AbstractChannel$AnnotatedConnectException: Connection refused: localhost/0:0:0:0:0:0:0:1:443
Caused by: java.net.ConnectException: Connection refused
```


####From browser
Open your browser in [https://grpc.studiozdev.com] 
