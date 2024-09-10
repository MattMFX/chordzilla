package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.LeaveChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class LeaveChordServiceImpl extends LeaveChordGrpc.LeaveChordImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveChordServiceImpl.class);

    @Override
    public void leave(LeaveChordRequest request, StreamObserver<LeaveChordResponse> responseObserver) {
        try {
            LOGGER.info("Received request to leave chord from node.");
            SelfNode selfNode = SelfNode.getInstance();
            Node newPredecessor = NodeUtils.mapToNode(request.getPredecessor());
            if (newPredecessor.id().compareTo(selfNode.id()) == 0) {
                selfNode.setPredecessor(null);
            } else {
                selfNode.setPredecessor(newPredecessor);
            }

            LOGGER.info(
                    "Updated predecessor. Node {} now has successor {} and predecessor {}.",
                    selfNode.id(),
                    Objects.isNull(selfNode.getSuccessor()) ? "null" : selfNode.getSuccessor().id(),
                    Objects.isNull(selfNode.getPredecessor()) ? "null" : selfNode.getPredecessor().id()
            );

            responseObserver.onNext(LeaveChordResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving leave chord request: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while receiving leave chord request.")
                            .asRuntimeException()
            );
        }

    }
}
