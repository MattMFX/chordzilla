package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class AcknowledgeJoinClient {
    private final AcknowledgeJoinGrpc.AcknowledgeJoinBlockingStub blockingStub;

    public AcknowledgeJoinClient(Channel channel) {
        this.blockingStub = AcknowledgeJoinGrpc.newBlockingStub(channel);
    }

    public void joinOk(NodeInformation predecessor, NodeInformation successor) {
        AcknowledgeJoinResponse response = blockingStub.joinOk(AcknowledgeJoinRequest.newBuilder()
                .setPredecessor(predecessor)
                .setSuccessor(successor)
                .build());
    }
}
