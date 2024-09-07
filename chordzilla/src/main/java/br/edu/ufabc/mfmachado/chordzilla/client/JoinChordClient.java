package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

public class JoinChordClient {
    private final JoinChordGrpc.JoinChordBlockingStub blockingStub;
    private final JoinChordGrpc.JoinChordStub asyncStub;

    public JoinChordClient(Channel channel) {
        this.blockingStub = JoinChordGrpc.newBlockingStub(channel);
        this.asyncStub = JoinChordGrpc.newStub(channel);
    }

    public void join(NodeInformation newNode) {
        JoinChordResponse response = blockingStub.join(JoinChordRequest.newBuilder()
                .setNewNode(newNode)
                .build());
    }
}
