package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneResponse;
import io.grpc.Channel;

public class NotifyPredecessorNodeGoneClient {
    private final NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneBlockingStub blockingStub;

    public NotifyPredecessorNodeGoneClient(Channel channel) {
        this.blockingStub = NotifyPredecessorNodeGoneGrpc.newBlockingStub(channel);
    }

    public void nodeGone(NodeInformation successor) {
        NotifyPredecessorNodeGoneResponse response = blockingStub.nodeGone(NotifyPredecessorNodeGoneRequest.newBuilder()
                .setSuccessor(successor)
                .build());
    }
}
