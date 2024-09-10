package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataResponse;
import io.grpc.Channel;

import java.util.List;
import java.util.Map;

public class TransferDataClient {
    private final TransferDataGrpc.TransferDataBlockingStub blockingStub;
    private final TransferDataGrpc.TransferDataStub asyncStub;

    public TransferDataClient(Channel channel) {
        this.blockingStub = TransferDataGrpc.newBlockingStub(channel);
        this.asyncStub = TransferDataGrpc.newStub(channel);
    }

    public void transfer(List<Data> data) {
        TransferDataResponse response = blockingStub.transfer(TransferDataRequest.newBuilder()
                .addAllData(data)
                .build());

    }
}
