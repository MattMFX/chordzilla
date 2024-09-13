package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataRequest;
import com.google.protobuf.ByteString;
import io.grpc.Channel;

public class StoreDataClient {

    private final StoreDataGrpc.StoreDataBlockingStub blockingStub;

    public StoreDataClient(Channel channel) {
        this.blockingStub = StoreDataGrpc.newBlockingStub(channel);
    }

    public void storeData(String key, ByteString value) {
        blockingStub.store(
                StoreDataRequest.newBuilder()
                        .setData(
                                Data.newBuilder()
                                        .setKey(key)
                                        .setValue(value)
                                        .build()
                        ).build()
        );
    }
}
