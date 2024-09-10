package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import io.grpc.*;
import io.grpc.protobuf.services.ProtoReflectionService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class GrpcServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(GrpcServer.class);
    private static final Integer RETRY = 3;
    private static Server server;
    private static boolean isRunning = false;

    private final List<BindableService> services;
    private final Integer port;

    public static boolean isRunning() {
        return isRunning;
    }

    public void start() throws InterruptedException {
        if (serverIsRunning()) {
            return;
        }

        LOGGER.info("Starting gRPC server...");
        for (int i = 0; i < RETRY; i++) {
            try {
                startGrpcServer(port);
                break;
            } catch (IOException e) {
                LOGGER.error("Failed to start gRPC server: {}. Retrying...", e.getMessage());
            }
        }
        server.awaitTermination();
    }

    public void startAsync() {
        if (serverIsRunning()) {
            return;
        }

        new Thread(() -> {
            try {
                this.start();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    private boolean serverIsRunning() {
        if (GrpcServer.isRunning()) {
            LOGGER.info("gRPC server is already running.");
            return true;
        }

        return false;
    }

    public static void stop() {
        try {
            if (server != null) {
                LOGGER.info("Shutting down gRPC server...");
                server.shutdown().awaitTermination(30, TimeUnit.SECONDS);
                isRunning = false;
            }
        } catch (Exception e) {
            LOGGER.error("An error occurred while shutting down the gRPC server: {}", e.getMessage(), e);
        }
    }

    private void startGrpcServer(int port) throws IOException {
        ServerBuilder<?> serverBuilder = Grpc.newServerBuilderForPort(port, InsecureServerCredentials.create());
        services.forEach(serverBuilder::addService);
        serverBuilder.addService(ProtoReflectionService.newInstance());
        GrpcServer.server = serverBuilder.build();
        server.start();
        stopServerOnApplicationShutdown();
        GrpcServer.isRunning = true;
        LOGGER.info("The gRPC server started! Listening on port {}", port);
    }

    private void stopServerOnApplicationShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            LOGGER.error("*** shutting down gRPC server since JVM is shutting down");
            GrpcServer.stop();
            LOGGER.error("*** server shut down");
        }));
    }
}
