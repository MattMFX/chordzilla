package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.JoinChordServiceImpl;
import io.grpc.BindableService;

import java.util.List;

public class GrpcServerFactory {
    private List<BindableService> services;
    private Integer port;

    private GrpcServerFactory() {
    }

    public static GrpcServerFactory newFactory() {
        return new GrpcServerFactory();
    }

    public GrpcServerFactory withServices(List<BindableService> services) {
        this.services = services;
        return this;
    }

    public GrpcServerFactory withPort(Integer port) {
        this.port = port;
        return this;
    }

    public GrpcServer create() {
        return new GrpcServer(services, port);
    }
}
