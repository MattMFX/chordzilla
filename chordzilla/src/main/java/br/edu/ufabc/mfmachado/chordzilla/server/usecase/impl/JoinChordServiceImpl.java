package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.math.BigInteger;

public class JoinChordServiceImpl extends JoinChordGrpc.JoinChordImplBase {

    @Override
    public void join(JoinChordRequest joinChordRequest, StreamObserver<JoinChordResponse> responseObserver) {
        SelfNode selfNode = SelfNode.getInstance();
        BigInteger id = new BigInteger(joinChordRequest.getNewNode().getId());

        if (selfNode.isOwner(id)) {
            responseObserver.onNext(JoinChordResponse.newBuilder().build());
            responseObserver.onCompleted();
        } else if (selfNode.getId().compareTo(id) == 0) {
            responseObserver.onError(
                    Status.INVALID_ARGUMENT
                            .withDescription("A node already exists for the ID informed.")
                            .asRuntimeException()
            );
        } else {

        }
    }
}
