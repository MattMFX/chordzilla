package br.edu.ufabc.mfmachado.chordzilla.core.grcp;

import io.grpc.BindableService;

import java.util.List;

public class GrcpServiceListBuilder {
    private List<BindableService> services;

    private GrcpServiceListBuilder() {
    }

    public static GrcpServiceListBuilder newBuilder () {
        return new GrcpServiceListBuilder();
    }

    public GrcpServiceListBuilder addService(BindableService service) {
        services.add(service);
        return this;
    }

    public List<BindableService> build() {
        return services;
    }
}
