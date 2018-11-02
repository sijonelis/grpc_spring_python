package com.vilkazz.grpc.grpc;


import io.grpc.StatusRuntimeException;
import org.springframework.stereotype.Component;



import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class PythonOperationClient {

    private static final Logger logger = Logger.getLogger(PythonOperationClient.class.getName());

    private final ManagedChannel channel;
    private final PythonServiceGrpc.PythonServiceBlockingStub blockingStub;

    /** Construct client connecting to HelloWorld server at {@code host:port}. */
    public PythonOperationClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build());
    }

    /** Construct client for accessing HelloWorld server using the existing channel. */
    PythonOperationClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = PythonServiceGrpc.newBlockingStub(channel);
    }

    public void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    public void greet(String name) {
        logger.info("Will try to greet " + name + " ...");
        PythonRequest request = PythonRequest.newBuilder().setValue(1).build();
        PythonResponse response;
        try {
            response = blockingStub.requestPython(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        logger.info("Greeting: " + response.getRespone());
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception {
        PythonOperationClient client = new PythonOperationClient("localhost", 50051);
        try {
            /* Access a service running on the local machine on port 50051 */
            String user = "world";
            if (args.length > 0) {
                user = args[0]; /* Use the arg as the name to greet if provided */
            }
            client.greet(user);
        } finally {
            client.shutdown();
        }
    }
}
