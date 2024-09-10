package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataResponse;
import com.google.protobuf.ByteString;
import io.grpc.Channel;

public class StoreDataClient {
    private final StoreDataGrpc.StoreDataBlockingStub blockingStub;
    private final StoreDataGrpc.StoreDataStub asyncStub;

    public StoreDataClient(Channel channel) {
        this.blockingStub = StoreDataGrpc.newBlockingStub(channel);
        this.asyncStub = StoreDataGrpc.newStub(channel);
    }

    public void store(String key, byte[] value) {
        StoreDataResponse response = blockingStub.store(StoreDataRequest.newBuilder()
                .setData(
                        Data.newBuilder()
                                .setKey(key)
                                .setValue(ByteString.copyFrom(value))
                                .build()
                )
                .build());
    }
}
