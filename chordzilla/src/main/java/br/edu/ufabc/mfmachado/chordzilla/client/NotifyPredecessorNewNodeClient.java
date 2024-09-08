package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class NotifyPredecessorNewNodeClient {
    private final NotifyPredecessorNewNodeGrpc.NotifyPredecessorNewNodeBlockingStub blockingStub;
    private final NotifyPredecessorNewNodeGrpc.NotifyPredecessorNewNodeStub asyncStub;

    public NotifyPredecessorNewNodeClient(Channel channel) {
        this.blockingStub = NotifyPredecessorNewNodeGrpc.newBlockingStub(channel);
        this.asyncStub = NotifyPredecessorNewNodeGrpc.newStub(channel);
    }

    public void newNode(NodeInformation newNode) {
        NotifyPredecessorNewNodeResponse response = blockingStub.newNode(NotifyPredecessorNewNodeRequest.newBuilder()
                .setNewNode(newNode)
                .build());
    }
}
