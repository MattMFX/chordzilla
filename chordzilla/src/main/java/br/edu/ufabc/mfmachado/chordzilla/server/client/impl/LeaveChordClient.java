package br.edu.ufabc.mfmachado.chordzilla.server.client.impl;

import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import io.grpc.Channel;

public class LeaveChordClient {
    private final LeaveChordGrpc.LeaveChordBlockingStub blockingStub;

    public LeaveChordClient(Channel channel) {
        this.blockingStub = LeaveChordGrpc.newBlockingStub(channel);
    }

    public void leaveChord(NodeInformation predecessor) {
        LeaveChordResponse response = blockingStub.leave(LeaveChordRequest.newBuilder()
                .setPredecessor(predecessor)
                .build());
    }
}
