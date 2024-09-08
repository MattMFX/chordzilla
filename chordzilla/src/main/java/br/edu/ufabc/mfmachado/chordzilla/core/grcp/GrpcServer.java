package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import io.grpc.*;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.RequiredArgsConstructor;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class GrpcServer {
    private static final Integer RETRY = 3;
    private Server server;

    private final List<BindableService> services;
    private final Integer port;

    public void start() throws InterruptedException {
        System.out.println("Starting gRPC server...");
        for (int i = 0; i < RETRY; i++) {
            try {
                startGrpcServer(port);
                break;
            } catch (IOException e) {
                System.err.println("Failed to start gRPC server: " + e.getMessage() + ". Retrying...");
            }
        }
        server.awaitTermination();
    }

    public void startAsync() {
        new Thread(() -> {
            try {
                this.start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void startGrpcServer(int port) throws IOException {
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create());
        services.forEach(serverBuilder::addService);
        serverBuilder.addService(ProtoReflectionService.newInstance());
        this.server = serverBuilder.build();
        server.start();
        stopServerOnApplicationShutdown();
        System.out.println("The gRPC server started! Listening on port " + port);
    }

    private void stopServerOnApplicationShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.err.println("*** shutting down gRPC server since JVM is shutting down");
            try {
                GrpcServer.this.stop();
            } catch (InterruptedException e) {
                e.printStackTrace(System.err);
            }
            System.err.println("*** server shut down");
        }));
    }
}
