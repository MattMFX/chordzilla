package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundResponse;
import io.grpc.Channel;

public class RetrieveDataNotFoundClient {
    private final RetrieveDataNotFoundGrpc.RetrieveDataNotFoundBlockingStub blockingStub;

    public RetrieveDataNotFoundClient(Channel channel) {
        this.blockingStub = RetrieveDataNotFoundGrpc.newBlockingStub(channel);
    }

    public void notFound(String key) {
        RetrieveDataNotFoundResponse response = blockingStub.notFound(RetrieveDataNotFoundRequest.newBuilder()
                .setKey(key)
                .build());
    }
}
