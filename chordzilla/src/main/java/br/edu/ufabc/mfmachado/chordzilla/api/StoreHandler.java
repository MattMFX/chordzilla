package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.client.StoreDataClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class StoreHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(StoreHandler.class);

    public void store(String key, byte[] value) {
        try {
            LOGGER.info("Storing value in the chord.");
            SelfNode selfNode = SelfNode.getInstance();
            Channel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create()).build();
            StoreDataClient storeDataClient = new StoreDataClient(channel);
            storeDataClient.store(Client.getHashStrategy().hash(key.getBytes()).toString(), value);
            LOGGER.info("Value stored successfully.");
        } catch (Exception e) {
            LOGGER.info("Error while storing value in chord.");
        }
    }
}
