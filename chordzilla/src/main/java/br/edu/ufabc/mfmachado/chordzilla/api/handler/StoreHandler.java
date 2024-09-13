package br.edu.ufabc.mfmachado.chordzilla.api.handler;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.StoreDataResponse;
import com.google.protobuf.ByteString;
import io.grpc.Channel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executors;

public class StoreHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreHandler.class);
    private final StoreDataGrpc.StoreDataBlockingStub blockingStub;

    public StoreHandler(Channel channel) {
        this.blockingStub = StoreDataGrpc.newBlockingStub(channel);
    }

    public void store(String key, byte[] value) {
        try {
            LOGGER.info("Storing value in the chord.");
            sendStoreRequest(key, value);
            LOGGER.info("Value stored successfully.");
        } catch (Exception e) {
            LOGGER.info("Error while storing value in chord: {}", e.getMessage(), e);
            throw new RuntimeException("Error while storing value in chord", e);
        }
    }

    public void sendStoreRequest(String key, byte[] value) {
        String hashKey = Client.getHashStrategy().hash(key.getBytes()).toString();
        StoreDataResponse response = blockingStub.store(
                StoreDataRequest.newBuilder()
                    .setData(
                            Data.newBuilder()
                                    .setKey(hashKey)
                                    .setValue(ByteString.copyFrom(value))
                                    .build()
                    ).build()
        );
    }
}
