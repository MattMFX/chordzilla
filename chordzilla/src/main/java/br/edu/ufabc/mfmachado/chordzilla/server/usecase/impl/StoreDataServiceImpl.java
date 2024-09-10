package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.client.StoreDataClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataResponse;
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
        try {
            LOGGER.info("Received data store request for ID {}", request.getData().getKey());
            SelfNode selfNode = SelfNode.getInstance();
            BigInteger key = new BigInteger(request.getData().getKey());

            if (selfNode.isOwner(key)) {
                selfNode.addData(key, request.getData().getValue().toByteArray());
                LOGGER.info("Data stored successfully");
            } else {
                LOGGER.info("Data does not belong to this node, forwarding to successor.");
                store(selfNode.getSuccessor(), request.getData());
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
        }
    }

    /**
     * Transfere para o novo nó os dados que pertencem a ele.
     */
    private void store(Node successor, Data data) {
        Channel channel = null;
        try {
            channel = getChannel(successor.ip(), successor.port());
            StoreDataClient storeDataClient = new StoreDataClient(channel);
            storeDataClient.store(data.getKey(), data.getValue().toByteArray());
        } finally {
            if (channel instanceof ManagedChannel) {
                ((ManagedChannel) channel).shutdown();
            }
        }
    }

    private Channel getChannel(String ip, Integer port) {
        return Grpc.newChannelBuilder(NodeUtils.getNodeAdress(ip, port), InsecureChannelCredentials.create()).build();
    }
}
