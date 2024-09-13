package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataResponse;
import io.grpc.Channel;

public class RetrieveDataClient {
    private final RetrieveDataGrpc.RetrieveDataBlockingStub blockingStub;

    public RetrieveDataClient(Channel channel) {
        this.blockingStub = RetrieveDataGrpc.newBlockingStub(channel);
    }

    public void retrieve(String key, NodeInformation client) {
        SelfNode selfNode = SelfNode.getInstance();
         RetrieveDataResponse response = blockingStub.retrieve(RetrieveDataRequest.newBuilder()
                 .setNode(client)
                 .setKey(key)
                 .build());
    }
}
