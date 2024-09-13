package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class NotifyPredecessorNewNodeClient {
    private final NotifyPredecessorNewNodeGrpc.NotifyPredecessorNewNodeBlockingStub blockingStub;

    public NotifyPredecessorNewNodeClient(Channel channel) {
        this.blockingStub = NotifyPredecessorNewNodeGrpc.newBlockingStub(channel);
    }

    public void newNode(NodeInformation newNode) {
        NotifyPredecessorNewNodeResponse response = blockingStub.newNode(NotifyPredecessorNewNodeRequest.newBuilder()
                .setNewNode(newNode)
                .build());
    }
}
