package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.server.client.ChordClientManager;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.RetrieveDataClient;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.RetrieveDataNotFoundClient;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.RetrieveDataOkClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import com.google.protobuf.ByteString;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class RetrieveDataServiceImpl extends RetrieveDataGrpc.RetrieveDataImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveDataServiceImpl.class);

    @Override
    public void retrieve(RetrieveDataRequest request, StreamObserver<RetrieveDataResponse> responseObserver) {
        ChordClientManager clientManager = null;
        try {
            LOGGER.info("Received data retrieval request for node {} and key {}.", request.getNode().getId(), request.getKey());
            SelfNode selfNode = SelfNode.getInstance();
            BigInteger key = new BigInteger(request.getKey());

            if (selfNode.isOwner(key)) {
                LOGGER.info("Data belongs to this node, retrieving.");
                clientManager = ChordClientManager.withHost(
                        request.getNode().getIp(),
                        request.getNode().getPort()
                );
                byte[] value = selfNode.getData(key);
                if (value == null) {
                    LOGGER.info("Data not found.");
                    clientManager.getRetrieveDataNotFoundClient().notFound(request.getKey());
                } else {
                    LOGGER.info("Data found.");
                    clientManager.getRetrieveDataOkClient().ok(request.getKey(), ByteString.copyFrom(value));
                }
            } else {
                LOGGER.info("Data does not belong to this node, forwarding to successor.");
                clientManager = ChordClientManager.withHost(
                        selfNode.getSuccessor().ip(),
                        selfNode.getSuccessor().port()
                );
                clientManager.getRetrieveDataClient().retrieve(request.getKey(), request.getNode());
            }

            responseObserver.onNext(RetrieveDataResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while retrieving data: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while retrieving data.")
                            .asRuntimeException()
            );
        } finally {
            if (clientManager != null) {
                clientManager.closeClient();
            }
        }
    }
}
