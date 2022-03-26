package com.studiozdev.grpc.webdemo;

import com.studiozdev.grpc.webdemo.helloworld.GreeterGrpc;
import com.studiozdev.grpc.webdemo.helloworld.HelloReply;
import com.studiozdev.grpc.webdemo.helloworld.HelloRequest;
import com.sun.org.apache.xpath.internal.operations.Bool;
import io.grpc.Channel;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.netty.NegotiationType;
import io.grpc.netty.NettyChannelBuilder;
import io.netty.handler.codec.http2.Http2SecurityUtil;
import io.netty.handler.ssl.*;

import java.io.File;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * A simple client that requests a greeting from the {@link HostnameServer}.
 */
public class GreeterClient {
    private static final Logger logger = Logger.getLogger(GreeterClient.class.getName());

    private final GreeterGrpc.GreeterBlockingStub blockingStub;

    /**
     * Construct client for accessing HelloWorld server using the existing channel.
     */
    public GreeterClient(Channel channel) {
        // 'channel' here is a Channel, not a ManagedChannel, so it is not this code's responsibility to
        // shut it down.

        // Passing Channels to code makes code easier to test and makes it easier to reuse Channels.
        blockingStub = GreeterGrpc.newBlockingStub(channel);
    }

    /**
     * Say hello to server.
     */
    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        HelloRequest request = HelloRequest.newBuilder().setName(name).build();
        HelloReply response;
        try {
            response = blockingStub.sayHello(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getMessage());
    }


    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting. The second argument is the target server.
     */
    public static void main(String[] args) throws Exception {
        String hostname = System.getenv("HOSTNAME");
        //hostname = "grpc.studiozdev.com";
        String port = System.getenv("PORT");
        //port = "443";
        String plaintext = System.getenv("USE_PLAINTEXT");
        //plaintext = "false";
        System.out.println("hostname = " + hostname);
        System.out.println("port = " + port);
        System.out.println("plaintext = " + plaintext);

        boolean usePlaintext = true;

        if (hostname == null) {
            hostname = "localhost";
        }
        if (port == null) {
            port = "50051";
        }
        if (plaintext == null) {
            usePlaintext = true;
        } else {
            boolean b = Boolean.parseBoolean(plaintext);
            if (b == false) {
                usePlaintext = false;
            }
        }

        System.out.println("usePlaintext = " + usePlaintext);


        String user = "world";
        // Access a service running on the local machine on port 50051
        String target = hostname + ":" + port;
        // Allow passing in the user and target strings as command line arguments
        if (args.length > 0) {
            if ("--help".equals(args[0])) {
                System.err.println("Usage: [name [target]]");
                System.err.println("");
                System.err.println("  name    The name you wish to be greeted by. Defaults to " + user);
                System.err.println("  target  The server to connect to. Defaults to " + target);
                System.exit(1);
            }
            user = args[0];
        }
        if (args.length > 1) {
            target = args[1];
        }

        // Create a communication channel to the server, known as a Channel. Channels are thread-safe
        // and reusable. It is common to create channels at the beginning of your application and reuse
        // them until the application shuts down.
        ManagedChannel channel = null;
        if (usePlaintext) {
            System.out.println("creating channel for plaintext");
            channel = ManagedChannelBuilder.forTarget(target)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .usePlaintext()
                    .build();
        } else {
            System.out.println("creating channel for TLS");
            SslProvider provider =  SslProvider.isAlpnSupported(SslProvider.OPENSSL)  ? SslProvider.OPENSSL : SslProvider.JDK;
            System.out.println("provider = " + provider);
            File ca = new File("/run/secrets/nginx.cert");
            if(hostname == "localhost") {
                ca = new File("/run/secrets/nginx-selfsigned.cert");
            }

            SslContext sslcontext = SslContextBuilder.forClient()
                    .sslProvider(provider)
                    .ciphers(Http2SecurityUtil.CIPHERS, SupportedCipherSuiteFilter.INSTANCE).trustManager(ca)
                    .applicationProtocolConfig(
                            new ApplicationProtocolConfig(ApplicationProtocolConfig.Protocol.ALPN, ApplicationProtocolConfig.SelectorFailureBehavior.NO_ADVERTISE,
                                    ApplicationProtocolConfig.SelectedListenerFailureBehavior.ACCEPT, ApplicationProtocolNames.HTTP_2)).build();
            //SslContext sslcontext = SslContextBuilder.forClient().trustManager(ca).build();
            channel = NettyChannelBuilder.forTarget(target).sslContext(sslcontext)
                    // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
                    // needing certificates.
                    .build();
        }

        try {
            GreeterClient client = new GreeterClient(channel);
            client.greet(user);
        } finally {
            // ManagedChannels use resources like threads and TCP connections. To prevent leaking these
            // resources the channel should be shut down when it will no longer be used. If it may be used
            // again leave it running.
            channel.shutdownNow().awaitTermination(5, TimeUnit.SECONDS);
        }
    }
}
