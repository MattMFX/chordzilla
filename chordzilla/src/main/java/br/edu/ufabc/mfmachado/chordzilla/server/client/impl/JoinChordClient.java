package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class JoinChordClient {
    private final JoinChordGrpc.JoinChordBlockingStub blockingStub;

    public JoinChordClient(Channel channel) {
        this.blockingStub = JoinChordGrpc.newBlockingStub(channel);
    }

    public void join(NodeInformation newNode) {
        JoinChordResponse response = blockingStub.join(JoinChordRequest.newBuilder()
                .setNewNode(newNode)
                .build());
    }
}
