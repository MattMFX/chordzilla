package br.edu.ufabc.mfmachado.chordzilla.api.handler;

import br.edu.ufabc.mfmachado.chordzilla.api.model.ChordHost;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

@RequiredArgsConstructor
public class JoinChordHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinChordHandler.class);
    private final ChordInitializerService chordInitializerService;

    public void newChord() {
        try {
            chordInitializerService.initialize();
        } catch (Exception e) {
            LOGGER.error("Error initializing chord node: {}", e.getMessage(), e);
        }
    }

    public void join(String targetIp, Integer targetPort) {
        try {
            chordInitializerService.initialize();
            SelfNode selfNode = SelfNode.getInstance();
            sendJoinRequest(selfNode, targetIp, targetPort);
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network.", e.getMessage());
            chordInitializerService.initialize();
        }
    }

    public void join(List<ChordHost> chordHosts) {
        try {
            boolean joined = chordHosts.stream()
                    .map(this::tryJoin)
                    .reduce((b1, b2) -> b1 || b2)
                    .orElse(false);

            if (joined) {
                return;
            }

            throw new IllegalArgumentException("No nodes available to join in the known host list provided");
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network", e.getMessage());
            chordInitializerService.initialize();
        }
    }

    private boolean tryJoin(ChordHost chordHost) {
        try {
            chordInitializerService.initialize();
            SelfNode selfNode = SelfNode.getInstance();
            sendJoinRequest(selfNode, chordHost.ip(), chordHost.port());
            return true;
        } catch (Exception e) {
            LOGGER.info("Host {}:{} is not available to join", chordHost.ip(), chordHost.port());
            return false;
        }
    }

    private void sendJoinRequest(SelfNode selfNode, String targetIp, Integer targetPort) {
        LOGGER.info("Joining the network");
        ManagedChannel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(targetIp, targetPort), InsecureChannelCredentials.create()).build();
        JoinChordGrpc.JoinChordBlockingStub blockingStub = JoinChordGrpc.newBlockingStub(channel);
        JoinChordResponse response = blockingStub.join(
                JoinChordRequest.newBuilder().setNewNode(
                        NodeInformation.newBuilder()
                                .setId(selfNode.id().toString())
                                .setIp(selfNode.ip())
                                .setPort(selfNode.port())
                                .build()
                ).build()
        );
        LOGGER.info(
                "Successfully joined the network. Node is running on {}:{} with ID {}",
                selfNode.ip(),
                selfNode.port(),
                selfNode.id()
        );
        channel.shutdown();
    }
}
