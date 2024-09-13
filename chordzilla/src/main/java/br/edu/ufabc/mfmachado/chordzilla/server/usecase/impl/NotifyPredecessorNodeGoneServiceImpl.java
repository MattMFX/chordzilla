package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNewNodeResponse;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

public class NotifyPredecessorNodeGoneServiceImpl extends NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyPredecessorNodeGoneServiceImpl.class);

    @Override
    public void nodeGone(NotifyPredecessorNodeGoneRequest request, StreamObserver<NotifyPredecessorNodeGoneResponse> responseObserver) {
        try {
            LOGGER.info("Received notification of node gone.");
            SelfNode selfNode = SelfNode.getInstance();
            Node newSuccessor = NodeUtils.mapToNode(request.getSuccessor());

            if (newSuccessor.id().compareTo(selfNode.id()) == 0) {
                selfNode.setSuccessor(null);
            } else {
                selfNode.setSuccessor(newSuccessor);
            }

            LOGGER.info(
                    "Updated successor. Node {} now has successor {} and predecessor {}.",
                    selfNode.id(),
                    Objects.isNull(selfNode.getSuccessor()) ? null : selfNode.getSuccessor().id(),
                    Objects.isNull(selfNode.getPredecessor()) ? null : selfNode.getPredecessor().id()
            );

            responseObserver.onNext(NotifyPredecessorNodeGoneResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving notification of node gone: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while receiving notification of node gone.")
                            .asRuntimeException()
            );
        }
    }
}
