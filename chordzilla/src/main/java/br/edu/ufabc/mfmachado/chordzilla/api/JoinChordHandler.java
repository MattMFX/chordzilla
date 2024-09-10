package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.client.JoinChordClient;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.node.ChordNode;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class JoinChordHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinChordHandler.class);
    private static final String SUCCESS_MESSAGE = "Successfully joined the network";
    private final ChordInitializerService chordInitializerService;

    public void join(String ip, Integer port) {
        try {
            LOGGER.info("Joining the network");
            Channel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(ip, port), InsecureChannelCredentials.create()).build();
            JoinChordClient joinChordClient = new JoinChordClient(channel);

            initNode();
            SelfNode selfNode = SelfNode.getInstance();

            sendJoinRequest(joinChordClient, selfNode);
            LOGGER.info(SUCCESS_MESSAGE);
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network.", e.getMessage(), e);
            initNode();
        }
    }

    private void initNode() {
        try {
            chordInitializerService.initialize();
        } catch (Exception e) {
            LOGGER.error("Error initializing chord node: {}", e.getMessage(), e);
        }
    }

    private void sendJoinRequest(JoinChordClient client, Node node) {
        client.join(NodeInformation.newBuilder()
                .setId(node.id().toString())
                .setIp(node.ip())
                .setPort(node.port())
                .build()
        );
    }

    public void join(List<Node> nodes) {
        try {
            boolean joined = nodes.stream()
                    .map(this::tryJoin)
                    .reduce((b1, b2) -> b1 || b2)
                    .orElse(false);

            if (joined) {
                LOGGER.info(SUCCESS_MESSAGE);
                return;
            }

            throw new IllegalArgumentException("No nodes available to join in the known host list provided");
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network", e.getMessage(), e);
            initNode();
        }
    }

    private boolean tryJoin(Node node) {
        try {
            Channel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(node.ip(), node.port()), InsecureChannelCredentials.create()).build();
            JoinChordClient joinChordClient = new JoinChordClient(channel);

            initNode();
            SelfNode selfNode = SelfNode.getInstance();

            sendJoinRequest(joinChordClient, selfNode);
            return true;
        } catch (Exception e) {
            LOGGER.info("Host {}:{} is not available to join", node.ip(), node.port());
            return false;
        }
    }
}
