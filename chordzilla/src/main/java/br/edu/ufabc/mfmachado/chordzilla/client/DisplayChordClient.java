package br.edu.ufabc.mfmachado.chordzilla.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import io.grpc.Channel;

import java.util.List;

public class DisplayChordClient {
    private final DisplayChordGrpc.DisplayChordBlockingStub blockingStub;
    private final DisplayChordGrpc.DisplayChordStub asyncStub;

    public DisplayChordClient(Channel channel) {
        this.blockingStub = DisplayChordGrpc.newBlockingStub(channel);
        this.asyncStub = DisplayChordGrpc.newStub(channel);
    }

    public void display() {
        DisplayChordResponse response = blockingStub.display(DisplayChordRequest.newBuilder().build());
    }

    public void display(List<String> ids) {
        DisplayChordResponse response = blockingStub.display(DisplayChordRequest.newBuilder()
                .addAllId(ids)
                .build());
    }
}
