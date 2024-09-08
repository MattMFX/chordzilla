package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NotifyPredecessorNewNodeServiceImpl extends NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(NotifyPredecessorNewNodeServiceImpl.class);

    @Override
    public void nodeGone(NotifyPredecessorNodeGoneRequest request, StreamObserver<NotifyPredecessorNodeGoneResponse> responseObserver) {
        try {
            SelfNode selfNode = SelfNode.getInstance();
            selfNode.setSuccessor(NodeUtils.mapToChordNode(request.getSuccessor()));
            responseObserver.onNext(NotifyPredecessorNodeGoneResponse.newBuilder().build());
            responseObserver.onCompleted();
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
