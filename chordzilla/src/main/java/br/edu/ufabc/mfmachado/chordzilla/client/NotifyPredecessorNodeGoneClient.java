package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneResponse;
import io.grpc.Channel;

public class NotifyPredecessorNodeGoneClient {
    private final NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneBlockingStub blockingStub;
    private final NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneStub asyncStub;

    public NotifyPredecessorNodeGoneClient(Channel channel) {
        this.blockingStub = NotifyPredecessorNodeGoneGrpc.newBlockingStub(channel);
        this.asyncStub = NotifyPredecessorNodeGoneGrpc.newStub(channel);
    }

    public void nodeGone(NodeInformation successor) {
        NotifyPredecessorNodeGoneResponse response = blockingStub.nodeGone(NotifyPredecessorNodeGoneRequest.newBuilder()
                .setSuccessor(successor)
                .build());
    }
}
