package br.edu.ufabc.mfmachado.chordzilla.api.handler;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.DataUtils;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

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
            updateSuccessor(selfNode);
            updatePredecessor(selfNode);
            GrpcServer.stop();
            LOGGER.info("Successfully left the network");
        } catch (Exception e) {
            LOGGER.info("Error leaving the network");
            throw new RuntimeException("Error leaving the network", e);
        }
    }

    private void updateSuccessor(SelfNode selfNode) {
        if (Objects.isNull(selfNode.getSuccessor())) {
            return;
        }

        // Transfere para o novo nó os dados antes de sair do chord.
        LOGGER.info("Transferring data to successor...");
        List<BigInteger> keys = selfNode.getKeys();
        List<Data> data = keys.stream()
                .map(key -> {
                    LOGGER.info("Transferring data for key {} to successor...", key);
                    return DataUtils.mapToData(key, selfNode.popData(key));
                })
                .toList();

        if (!data.isEmpty()) {
            transfer(NodeUtils.mapToNodeInformation(selfNode.getSuccessor()), data);
            LOGGER.info("Data transferred to successor");
        }

        ManagedChannel successorChannel = getChannel(selfNode.getSuccessor().ip(), selfNode.getSuccessor().port());
        LeaveChordGrpc.LeaveChordBlockingStub blockingStub = LeaveChordGrpc.newBlockingStub(successorChannel);
        LeaveChordResponse response = blockingStub.leave(LeaveChordRequest.newBuilder().setPredecessor(NodeUtils.mapToNodeInformation(selfNode.getPredecessor())).build());
        successorChannel.shutdown();
    }

    private void updatePredecessor(SelfNode selfNode) {
        if (Objects.isNull(selfNode.getPredecessor())) {
            return;
        }
        ManagedChannel predecessorChannel = getChannel(selfNode.getPredecessor().ip(), selfNode.getPredecessor().port());
        NotifyPredecessorNodeGoneGrpc.NotifyPredecessorNodeGoneBlockingStub blockingStub =
                NotifyPredecessorNodeGoneGrpc.newBlockingStub(predecessorChannel);
        NotifyPredecessorNodeGoneResponse response = blockingStub.nodeGone(
                NotifyPredecessorNodeGoneRequest
                        .newBuilder()
                        .setSuccessor(NodeUtils.mapToNodeInformation(selfNode.getSuccessor()))
                        .build()
        );
        predecessorChannel.shutdown();
    }

    /**
     * Transfere para o novo nó os dados que pertencem a ele.
     */
    private void transfer(NodeInformation joining, List<Data> data) {
        ManagedChannel channel = getChannel(joining.getIp(), joining.getPort());
        TransferDataGrpc.TransferDataBlockingStub blockingStub = TransferDataGrpc.newBlockingStub(channel);
        TransferDataResponse response = blockingStub.transfer(TransferDataRequest.newBuilder().addAllData(data).build());
        channel.shutdown();
    }

    private ManagedChannel getChannel(String ip, Integer port) {
        return Grpc.newChannelBuilder(NodeUtils.getNodeAdress(ip, port), InsecureChannelCredentials.create()).build();
    }
}
