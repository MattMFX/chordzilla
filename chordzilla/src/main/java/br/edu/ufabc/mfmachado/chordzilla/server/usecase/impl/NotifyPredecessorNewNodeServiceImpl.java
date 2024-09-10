package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNewNodeGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNewNodeRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNewNodeResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyPredecessorNewNodeServiceImpl extends NotifyPredecessorNewNodeGrpc.NotifyPredecessorNewNodeImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyPredecessorNewNodeServiceImpl.class);

    @Override
    public void newNode(NotifyPredecessorNewNodeRequest request, StreamObserver<NotifyPredecessorNewNodeResponse> responseObserver) {
        try {
            LOGGER.info("Received notification that new node {} joined.", request.getNewNode().getId());
            SelfNode selfNode = SelfNode.getInstance();
            selfNode.setSuccessor(NodeUtils.mapToNode(request.getNewNode()));
            responseObserver.onNext(NotifyPredecessorNewNodeResponse.newBuilder().build());
            responseObserver.onCompleted();
            LOGGER.info(
                    "Updated successor. Node {} now has successor {} and predecessor {}.",
                    selfNode.id(),
                    selfNode.getSuccessor().id(),
                    selfNode.getPredecessor().id()
            );
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving notification: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while receiving notification of node gone")
                            .asRuntimeException()
            );
        }
    }
}
