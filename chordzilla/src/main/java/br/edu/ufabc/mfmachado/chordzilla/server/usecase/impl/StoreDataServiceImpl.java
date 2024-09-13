package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.client.ChordClientManager;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class StoreDataServiceImpl extends StoreDataGrpc.StoreDataImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreDataServiceImpl.class);

    @Override
    public void store(StoreDataRequest request, StreamObserver<StoreDataResponse> responseObserver) {
        ChordClientManager clientManager = null;
        try {
            LOGGER.info("Received data store request for ID {}", request.getData().getKey());
            SelfNode selfNode = SelfNode.getInstance();
            BigInteger key = new BigInteger(request.getData().getKey());

            if (selfNode.isOwner(key)) {
                selfNode.addData(key, request.getData().getValue().toByteArray());
                LOGGER.info("Data stored successfully with key {}.", key);
            } else {
                LOGGER.info("Data does not belong to this node, forwarding to successor.");
                clientManager = ChordClientManager.withHost(
                        selfNode.getSuccessor().ip(),
                        selfNode.getSuccessor().port()
                );
                clientManager.getStoreDataClient().storeData(request.getData().getKey(), request.getData().getValue());
            }

            responseObserver.onNext(StoreDataResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while storing data: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while storing data.")
                            .asRuntimeException()
            );
        } finally {
            if (clientManager != null) {
                clientManager.closeClient();
            }
        }
    }
}
