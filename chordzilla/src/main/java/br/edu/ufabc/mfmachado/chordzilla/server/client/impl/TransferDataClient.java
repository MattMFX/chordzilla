package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataResponse;
import io.grpc.Channel;

import java.util.List;

public class TransferDataClient {
    private final TransferDataGrpc.TransferDataBlockingStub blockingStub;

    public TransferDataClient(Channel channel) {
        this.blockingStub = TransferDataGrpc.newBlockingStub(channel);
    }

    public void transfer(List<Data> data) {
        TransferDataResponse response = blockingStub.transfer(TransferDataRequest.newBuilder()
                .addAllData(data)
                .build());

    }
}
