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

    /** Construct client connecting to the GRPC server at {@code host:port}. */
    public PythonOperationClient(String host, int port) {
        this(ManagedChannelBuilder.forAddress(host, port)
            // Channels are secure by default (via SSL/TLS). For the example we disable TLS to avoid
            // needing certificates.
            .usePlaintext()
            .build());
    }

    /** Construct client for accessing GRPC server using the existing channel. */
    PythonOperationClient(ManagedChannel channel) {
        this.channel = channel;
        blockingStub = PythonServiceGrpc.newBlockingStub(channel);
    }

    PythonOperationClient() {
        this.channel = null;
        blockingStub = null;
    }

    private void shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    /** Say hello to server. */
    private void requestPythonOperation(String operationType, int value, int operand) {
        logger.info("Will try to perform " + operationType + " with " + value + " and " + operand + " ...");
        PythonRequest request = PythonRequest.newBuilder().setType(operationType).setValue(value).setOperand(operand).build();
        PythonResponse response;
        try {
            response = blockingStub.requestPython(request);
        } catch (StatusRuntimeException e) {
            logger.log(Level.WARNING, "RPC failed: {0}", e.getStatus());
            return;
        }
        if (!response.getError().isEmpty()) {
            logger.warning("ERROR: " + response.getError());
        }
        logger.info("Result: " + response.getResponse());
    }

    /**
     * Greet server. If provided, the first element of {@code args} is the name to use in the
     * greeting.
     */
    public static void main(String[] args) throws Exception {
        PythonOperationClient client = new PythonOperationClient("localhost", 50051);
        try {
            /* Access a service running on the local machine on port 50051 */
//            String user = "world";
//            if (args.length > 0) {
//                user = args[0]; /* Use the arg as the name to greet if provided */
//            }
            client.requestPythonOperation("add", 1, 1);
            client.requestPythonOperation("sub", 10, 8);
            client.requestPythonOperation("mul", 2, 1);
            client.requestPythonOperation("div", 10, 5);
            client.requestPythonOperation("foo", 0, 0);
        } finally {
            client.shutdown();
        }
    }
}
