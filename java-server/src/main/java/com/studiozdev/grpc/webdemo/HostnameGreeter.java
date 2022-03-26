package com.studiozdev.grpc.webdemo;

import com.studiozdev.grpc.webdemo.helloworld.GreeterGrpc;
import com.studiozdev.grpc.webdemo.helloworld.HelloReply;
import com.studiozdev.grpc.webdemo.helloworld.HelloRequest;

import io.grpc.stub.StreamObserver;
import java.io.IOException;
import java.net.InetAddress;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Greeter implementation which replies identifying itself with its hostname. */
public final class HostnameGreeter extends GreeterGrpc.GreeterImplBase {
  private static final Logger logger = Logger.getLogger(HostnameGreeter.class.getName());

  private final String serverName;

  public HostnameGreeter(String serverName) {
    if (serverName == null) {
      serverName = determineHostname();
    }
    this.serverName = serverName;
  }

  @Override
  public void sayHello(HelloRequest req, StreamObserver<HelloReply> responseObserver) {
    String msg = "Hello " + req.getName() + ", from " + serverName;
    HelloReply reply = HelloReply.newBuilder()
        .setMessage(msg)
        .build();
    System.out.println(msg);
    responseObserver.onNext(reply);
    responseObserver.onCompleted();
  }

  private static String determineHostname() {
    try {
      String hostname = System.getenv("HOSTNAME");
      if(hostname == null) {
        return InetAddress.getLocalHost().getHostName();
      }
      return  hostname;
    } catch (IOException ex) {
      logger.log(Level.INFO, "Failed to determine hostname. Will generate one", ex);
    }
    // Strange. Well, let's make an identifier for ourselves.
    return "generated-" + new Random().nextInt();
  }
}
