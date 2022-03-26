package com.studiozdev.grpc.webdemo.server;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.studiozdev.grpc.webdemo.HostnameGreeter;

import com.studiozdev.grpc.webdemo.helloworld.GreeterGrpc;
import com.studiozdev.grpc.webdemo.helloworld.HelloReply;
import com.studiozdev.grpc.webdemo.helloworld.HelloRequest;
import io.grpc.inprocess.InProcessChannelBuilder;
import io.grpc.inprocess.InProcessServerBuilder;
import io.grpc.testing.GrpcCleanupRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Unit tests for {@link HostnameGreeter}.
 *
 * <p>This is very similar to HelloWorldServerTest, so see it for more descriptions.
 */
@RunWith(JUnit4.class)
public class HostnameGreeterTest {
  @Rule
  public final GrpcCleanupRule grpcCleanup = new GrpcCleanupRule();

  private GreeterGrpc.GreeterBlockingStub blockingStub =
        GreeterGrpc.newBlockingStub(grpcCleanup.register(
            InProcessChannelBuilder.forName("hostname").directExecutor().build()));

  @Test
  public void sayHello_fixedHostname() throws Exception {
    grpcCleanup.register(
        InProcessServerBuilder.forName("hostname")
          .directExecutor().addService(new HostnameGreeter("me")).build().start());

    HelloReply reply =
        blockingStub.sayHello(HelloRequest.newBuilder().setName("you").build());
    assertEquals("Hello you, from me", reply.getMessage());
  }

  @Test
  public void sayHello_dynamicHostname() throws Exception {
    grpcCleanup.register(
        InProcessServerBuilder.forName("hostname")
          .directExecutor().addService(new HostnameGreeter(null)).build().start());

    // Just verifing the service doesn't crash
    HelloReply reply =
        blockingStub.sayHello(HelloRequest.newBuilder().setName("anonymous").build());
    assertTrue(reply.getMessage(), reply.getMessage().startsWith("Hello anonymous, from "));
  }
}
