package com.vilkazz.grpc.grpc;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

import java.io.IOException;
import java.util.logging.Logger;

public class PythonOperationServer {

    private static final Logger logger = Logger.getLogger(PythonOperationServer.class.getName());

    private Server server;

    private void start() throws IOException {
        /* The port on which the server should run */
        int port = 50052;
        server = ServerBuilder.forPort(port)
            .addService(new PythonOperationImpl())
            .build()
            .start();
        logger.info("Server started, listening on " + port);
        Runtime.getRuntime().addShutdownHook(new Thread() {
        @Override
        public void run() {
            // Use stderr here since the logger may have been reset by its JVM shutdown hook.
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            PythonOperationServer.this.stop();
            System.err.println("*** server shut down");
        }
    });
}

    private void stop() {
        if (server != null) {
            server.shutdown();
        }
    }

    /**
     * Await termination on the main thread since the grpc library uses daemon threads.
     */
    private void blockUntilShutdown() throws InterruptedException {
        if (server != null) {
            server.awaitTermination();
        }
    }

    /**
     * Main launches the server from the command line.
     */
    public static void main(String[] args) throws IOException, InterruptedException {
        final PythonOperationServer server = new PythonOperationServer();
        server.start();
        server.blockUntilShutdown();
    }

    static class PythonOperationImpl extends PythonServiceGrpc.PythonServiceImplBase {

        @Override
        public void requestPython(PythonRequest req, StreamObserver<PythonResponse> responseObserver) {
            int response = 0;
            String error = "";
            switch (req.getType()) {
                case "add":
                    response = req.getValue() + req.getOperand();
                    break;
                case "sub":
                    response = req.getValue() - req.getOperand();
                    break;
                case "mul":
                    response = req.getValue() * req.getOperand();
                    break;
                case "div":
                    response = req.getValue() / req.getOperand();
                    break;
                default:
                    error = "Unsupported operation";
            }
            PythonResponse reply = PythonResponse.newBuilder().setResponse(response).setError(error).build();
            responseObserver.onNext(reply);
            responseObserver.onCompleted();
        }
    }
}
