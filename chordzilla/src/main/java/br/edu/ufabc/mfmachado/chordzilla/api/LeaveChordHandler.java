package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.client.LeaveChordClient;
import br.edu.ufabc.mfmachado.chordzilla.client.NotifyPredecessorNodeGoneClient;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RequiredArgsConstructor
public class LeaveChordHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(LeaveChordHandler.class);

    public void leave() {
        try {
            if (!GrpcServer.isRunning()) {
                String error = "gRPC server is not running. Cannot leave the network.";
                LOGGER.error(error);
                throw new IllegalStateException(error);
            }

            LOGGER.info("Leaving the network");
            SelfNode selfNode = SelfNode.getInstance();
            Channel predecessorChannel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(selfNode.getPredecessor().ip(), selfNode.getPredecessor().port()), InsecureChannelCredentials.create()).build();
            Channel successorChannel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(selfNode.getSuccessor().ip(), selfNode.getSuccessor().port()), InsecureChannelCredentials.create()).build();

            LeaveChordClient leaveChordClient = new LeaveChordClient(successorChannel);
            NotifyPredecessorNodeGoneClient notifyPredecessorNodeGoneClient = new NotifyPredecessorNodeGoneClient(predecessorChannel);

            leaveChordClient.leaveChord(NodeUtils.mapToNodeInformation(selfNode.getPredecessor()));
            notifyPredecessorNodeGoneClient.nodeGone(NodeUtils.mapToNodeInformation(selfNode.getSuccessor()));

            GrpcServer.stop();
            LOGGER.info("Successfully left the network");
        } catch (Exception e) {
            LOGGER.info("Error leaving the network");
        }
    }
}
