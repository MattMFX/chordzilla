package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.client.AcknowledgeJoinClient;
import br.edu.ufabc.mfmachado.chordzilla.client.JoinChordClient;
import br.edu.ufabc.mfmachado.chordzilla.client.StoreClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.ChordNode;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.JoinChordResponse;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.Objects;

public class JoinChordServiceImpl extends JoinChordGrpc.JoinChordImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinChordServiceImpl.class);

    @Override
    public void join(JoinChordRequest joinChordRequest, StreamObserver<JoinChordResponse> responseObserver) {
        try {
            LOGGER.info(
                    "Joining new node to the chord with ID {}, IP {} and port {}",
                    joinChordRequest.getNewNode().getId(),
                    joinChordRequest.getNewNode().getIp(),
                    joinChordRequest.getNewNode().getPort()
            );
            SelfNode selfNode = SelfNode.getInstance();
            BigInteger id = new BigInteger(joinChordRequest.getNewNode().getId());

            if (selfNode.isOwner(id)) {
                acknowledgeJoin(
                        joinChordRequest.getNewNode(),
                        selfNode.getPredecessor(),
                        selfNode
                );
                selfNode.setPredecessor(NodeUtils.mapToChordNode(joinChordRequest.getNewNode()));
            } else if (selfNode.id().compareTo(id) == 0) {
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("A node already exists for the ID informed.")
                                .asRuntimeException()
                );
            } else {
                joinChord(joinChordRequest.getNewNode());
            }

            responseObserver.onNext(JoinChordResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while adding new node to the chord: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An unexpected error ocurred while adding new node to the chord")
                            .asRuntimeException()
            );
        }

    }

    private void acknowledgeJoin(NodeInformation joining, Node predecessor, Node successor) {
        Channel channel = getChannel(joining.getIp(), joining.getPort());
        AcknowledgeJoinClient acknowledgeJoinClient = new AcknowledgeJoinClient(channel);
        NodeInformation succesorInfo = NodeUtils.mapToNodeInformation(successor);
        acknowledgeJoinClient.joinOk(
                Objects.nonNull(predecessor) ? NodeUtils.mapToNodeInformation(predecessor) : succesorInfo,
                succesorInfo
        );
    }

    private void joinChord(NodeInformation joining) {
        Channel channel = getChannel(joining.getIp(), joining.getPort());
        JoinChordClient joinChordClient = new JoinChordClient(channel);
        joinChordClient.join(joining);
    }

    private Channel getChannel(String ip, Integer port) {
        return Grpc.newChannelBuilder(NodeUtils.getNodeAdress(ip, port), InsecureChannelCredentials.create()).build();
    }
}
