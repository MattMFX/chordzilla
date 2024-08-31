package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl.JoinChordServiceImpl;
import io.grpc.BindableService;

import java.util.List;

public class GrpcServerFactory {
    private GrpcServerFactory() {
    }

    public static GrpcServerFactory newFactory() {
        return new GrpcServerFactory();
    }

    public GrpcServer create() {
        List<BindableService> services = GrcpServiceListBuilder.newBuilder()
                .addService(new JoinChordServiceImpl())
                .build();
        return new GrpcServer(services);
    }
}
