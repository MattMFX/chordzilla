package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkResponse;
import com.google.protobuf.ByteString;
import io.grpc.Channel;

public class RetrieveDataOkClient {
    private final RetrieveDataOkGrpc.RetrieveDataOkBlockingStub blockingStub;

    public RetrieveDataOkClient(Channel channel) {
        this.blockingStub = RetrieveDataOkGrpc.newBlockingStub(channel);
    }

    public void ok(String key, ByteString value) {
        RetrieveDataOkResponse response = blockingStub.ok(RetrieveDataOkRequest.newBuilder()
                .setData(
                        Data.newBuilder()
                                .setKey(key)
                                .setValue(value)
                                .build()
                )
                .build());
    }
}
