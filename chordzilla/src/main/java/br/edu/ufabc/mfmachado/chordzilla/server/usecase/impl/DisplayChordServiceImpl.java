package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.server.client.ChordClientManager;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.DisplayChordClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.DisplayChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.DisplayChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.DisplayChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class DisplayChordServiceImpl extends DisplayChordGrpc.DisplayChordImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(DisplayChordServiceImpl.class);

    @Override
    public void display(DisplayChordRequest request, StreamObserver<DisplayChordResponse> responseObserver) {
        ChordClientManager clientManager = null;
        try {
            LOGGER.info("Received request to display chord");
            SelfNode selfNode = SelfNode.getInstance();
            List<String> ids = new ArrayList<>(request.getIdList());
            boolean containsAllNodes = ids.contains(selfNode.id().toString());

            if (containsAllNodes) {
                LOGGER.info("Chord nodes: ");
                ids.stream().map(id -> id + " ---> ").reduce((s1, s2) -> s1 + s2).ifPresent(LOGGER::info);
            } else {
                ids.add(selfNode.id().toString());
                LOGGER.info("Added self to display list, new list: {}", ids);
                clientManager = ChordClientManager.withHost(
                        selfNode.getSuccessor().ip(),
                        selfNode.getSuccessor().port()
                );
                DisplayChordClient displayChordClient = clientManager.getDisplayChordClient();
                if (!ids.isEmpty()) {
                    displayChordClient.display(ids);
                } else {
                    displayChordClient.display();
                }
            }

            responseObserver.onNext(DisplayChordResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while displaying chord: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while displaying chord")
                            .asRuntimeException()
            );
        } finally {
            if (clientManager != null) {
                clientManager.closeClient();
            }
        }
    }
}
