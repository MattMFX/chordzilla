package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import io.grpc.*;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class GrpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
    private static final Integer RETRY = 3;
    private Server server;

    private final List<BindableService> services;

    public void start() throws InterruptedException {
        LOGGER.info("Starting gRPC server...");
        for (int i = 0; i < RETRY; i++) {
            try {
                startGrpcServer();
                break;
            } catch (IOException e) {
                LOGGER.error("Failed to start gRPC server. Retrying...", e);
            }
        }
        server.awaitTermination();
    }

    public void stop() throws InterruptedException {
        if (server != null) {
            server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
        }
    }

    private void startGrpcServer() throws IOException {
        int port = new Random().nextInt(1024, 49515);
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create());
        services.forEach(serverBuilder::addService);
        serverBuilder.addService(ProtoReflectionService.newInstance());
        this.server = serverBuilder.build();
        server.start();
        stopServerOnApplicationShutdown();
        LOGGER.info("The gRPC server started! Listening on port {}", port);
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
