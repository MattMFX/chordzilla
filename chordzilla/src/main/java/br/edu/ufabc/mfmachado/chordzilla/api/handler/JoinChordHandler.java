package br.edu.ufabc.mfmachado.chordzilla.api.handler;

import br.edu.ufabc.mfmachado.chordzilla.api.model.ChordHost;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
public class JoinChordHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(JoinChordHandler.class);
    private final ChordInitializerService chordInitializerService;
    private ManagedChannel channel;

    public void newChord() {
        try {
            chordInitializerService.initialize();
        } catch (Exception e) {
            LOGGER.error("Error initializing chord node: {}", e.getMessage(), e);
        }
    }

    public void join(String targetIp, Integer targetPort) {
        try {
            channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(targetIp, targetPort), InsecureChannelCredentials.create()).build();
            chordInitializerService.initialize();
            SelfNode selfNode = SelfNode.getInstance();
            sendJoinRequest(selfNode, targetIp, targetPort);
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network.", e.getMessage());
            chordInitializerService.initialize();
        } finally {
            if (channel != null) {
                channel.shutdown();
            }
        }
    }

    public void join(List<ChordHost> chordHosts) {
        try {
            chordInitializerService.initialize();
            SelfNode selfNode = SelfNode.getInstance();
            boolean joined = false;
            for (int i = 0; !joined && i < chordHosts.size(); i++) {
                joined = tryJoin(selfNode, chordHosts.get(i));
            }
//            boolean joined = chordHosts.stream()
//                    .map(this::tryJoin)
//                    .reduce((b1, b2) -> b1 || b2)
//                    .orElse(false);

            if (joined) {
                return;
            }

            throw new IllegalArgumentException("No nodes available to join in the known host list provided");
        } catch (Exception e) {
            LOGGER.error("Error joining the network: {}. Starting new network", e.getMessage());
            chordInitializerService.initialize();
        }
    }

    private boolean tryJoin(Node selfNode, ChordHost chordHost) {
        try {
            channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(chordHost.ip(), chordHost.port()), InsecureChannelCredentials.create()).build();
            sendJoinRequest(selfNode, chordHost.ip(), chordHost.port());
            return true;
        } catch (Exception e) {
            LOGGER.info("Host {}:{} is not available to join", chordHost.ip(), chordHost.port());
            return false;
        } finally {
            if (channel != null) {
                channel.shutdown();
            }
        }
    }

    private void sendJoinRequest(Node selfNode, String targetIp, Integer targetPort) {
        LOGGER.info("Joining the network...");
        JoinChordGrpc.JoinChordBlockingStub blockingStub = JoinChordGrpc.newBlockingStub(channel).withDeadlineAfter(5L, TimeUnit.SECONDS);
        JoinChordResponse response = blockingStub
                .join(JoinChordRequest.newBuilder().setNewNode(NodeUtils.mapToNodeInformation(selfNode)).build());
        LOGGER.info(
                "Successfully joined the network with host {}:{}. Node is running on {}:{} with ID {}",
                targetIp,
                targetPort,
                selfNode.ip(),
                selfNode.port(),
                selfNode.id()
        );
        channel.shutdown();
    }
}
