package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class AcknowledgeJoinClient {
    private final AcknowledgeJoinGrpc.AcknowledgeJoinBlockingStub blockingStub;
    private final AcknowledgeJoinGrpc.AcknowledgeJoinStub asyncStub;

    public AcknowledgeJoinClient(Channel channel) {
        this.blockingStub = AcknowledgeJoinGrpc.newBlockingStub(channel);
        this.asyncStub = AcknowledgeJoinGrpc.newStub(channel);
    }

    public void joinOk(NodeInformation predecessor, NodeInformation successor) {
        AcknowledgeJoinResponse response = blockingStub.joinOk(AcknowledgeJoinRequest.newBuilder()
                .setPredecessor(predecessor)
                .setSuccessor(successor)
                .build());
    }
}
