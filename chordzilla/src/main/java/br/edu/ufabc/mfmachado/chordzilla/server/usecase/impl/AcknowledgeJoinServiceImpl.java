package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.server.client.ChordClientManager;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.NotifyPredecessorNewNodeClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.ChordNode;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.AcknowledgeJoinGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.AcknowledgeJoinRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.AcknowledgeJoinResponse;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AcknowledgeJoinServiceImpl extends AcknowledgeJoinGrpc.AcknowledgeJoinImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(AcknowledgeJoinServiceImpl.class);

    @Override
    public void joinOk(AcknowledgeJoinRequest request, StreamObserver<AcknowledgeJoinResponse> responseObserver) {
        ChordClientManager clientManager = null;
        try {
            LOGGER.info("Received confirmation to join the chord");
            ChordNode predecessor = NodeUtils.mapToNode(request.getPredecessor());
            ChordNode successor = NodeUtils.mapToNode(request.getSuccessor());

            SelfNode selfNode = SelfNode.getInstance();
            selfNode.setPredecessor(predecessor);
            selfNode.setSuccessor(successor);

            clientManager = ChordClientManager.withHost(predecessor.ip(), predecessor.port());
            clientManager.getNotifyPredecessorNewNodeClient().newNode(NodeUtils.mapToNodeInformation(selfNode));

            responseObserver.onNext(AcknowledgeJoinResponse.newBuilder().build());
            responseObserver.onCompleted();

            LOGGER.info("Joined chord with ID {}, successor {} and predecessor {}", selfNode.id(), selfNode.getSuccessor().id(), selfNode.getPredecessor().id());
        } catch (Exception e) {
            LOGGER.error("An error ocurred while adding new node to the chord: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while acknowledging the join")
                            .asRuntimeException()
            );
        } finally {
            if (clientManager != null) {
                clientManager.closeClient();
            }
        }
    }
}
