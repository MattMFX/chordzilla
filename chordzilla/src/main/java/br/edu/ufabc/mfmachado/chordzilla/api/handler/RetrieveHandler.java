package br.edu.ufabc.mfmachado.chordzilla.api.handler;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.api.model.Response;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

public class RetrieveHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveHandler.class);
    private final RetrieveDataGrpc.RetrieveDataBlockingStub blockingStub;

    public RetrieveHandler(Channel channel) {
        this.blockingStub = RetrieveDataGrpc.newBlockingStub(channel);
    }

    public Optional<byte[]> retrieve(String key) {
        try {
            LOGGER.info("Retrieving value from the chord.");
            SelfNode selfNode = SelfNode.getInstance();

            String hashKey = Client.getHashStrategy().hash(key.getBytes()).toString();
            Client.addPendingRequest(new BigInteger(hashKey), new CompletableFuture<>());
            RetrieveDataResponse response = blockingStub.retrieve(
                    RetrieveDataRequest.newBuilder()
                            .setKey(hashKey)
                            .setNode(NodeUtils.mapToNodeInformation(selfNode))
                            .build()
            );

            Response<Data> completed = Client.getCompleted(new BigInteger(hashKey));
            if (Response.StatusCode.OK.equals(completed.statusCode())) {
                LOGGER.info("Value retrieved successfully.");
                return Optional.of(completed.body().getValue().toByteArray());
            } else if (Response.StatusCode.NOT_FOUND.equals(completed.statusCode())) {
                LOGGER.info("Value not found in the chord.");
                return Optional.empty();
            } else {
                LOGGER.error("Error while retrieving value from chord.");
                return Optional.empty();
            }
        } catch (Exception e) {
            LOGGER.error("Error while retrieving value from chord: {}", e.getMessage(), e);
            return Optional.empty();
        }
    }
}
