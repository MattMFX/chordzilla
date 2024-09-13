package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.server.client.ChordClientManager;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.AcknowledgeJoinClient;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.JoinChordClient;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.TransferDataClient;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.*;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.DataUtils;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.*;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;

public class JoinChordServiceImpl extends JoinChordGrpc.JoinChordImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(JoinChordServiceImpl.class);

    @Override
    public void join(JoinChordRequest joinChordRequest, StreamObserver<JoinChordResponse> responseObserver) {
        ChordClientManager clientManager = null;
        try {
            LOGGER.info(
                    "Joining new node to the chord with ID {}, IP {} and port {}",
                    joinChordRequest.getNewNode().getId(),
                    joinChordRequest.getNewNode().getIp(),
                    joinChordRequest.getNewNode().getPort()
            );
            SelfNode selfNode = SelfNode.getInstance();
            BigInteger id = new BigInteger(joinChordRequest.getNewNode().getId());

            if (selfNode.isOwner(id)) { // Verifica se o nó atual é dono do ID do nó que está entrando na rede.
                // Atualiza o predecessor do nó atual.
                Node oldPredecessor = selfNode.getPredecessor();
                selfNode.setPredecessor(NodeUtils.mapToNode(joinChordRequest.getNewNode()));
                LOGGER.info(
                        "Node with ID {} now has successor {} and predecessor {}",
                        selfNode.id(),
                        Objects.isNull(selfNode.getSuccessor()) ? "null" : selfNode.getSuccessor().id(),
                        selfNode.getPredecessor().id()
                );

                // Informa ao novo nó que ele foi adicionado à rede.
                clientManager = ChordClientManager.withHost(
                        joinChordRequest.getNewNode().getIp(),
                        joinChordRequest.getNewNode().getPort()
                );
                clientManager.getAcknowledgeJoinClient().joinOk(
                        Objects.nonNull(oldPredecessor) ? NodeUtils.mapToNodeInformation(oldPredecessor) : NodeUtils.mapToNodeInformation(selfNode),
                        NodeUtils.mapToNodeInformation(selfNode)
                );

                // Transfere para o novo nó os dados que pertencem a ele.
                List<BigInteger> keys = selfNode.getKeys();
                List<Data> data = keys.stream()
                        .filter(key -> !selfNode.isOwner(key))
                        .map(key -> {
                            LOGGER.info("Transferring data for key {} to new node...", key);
                            return DataUtils.mapToData(key, selfNode.popData(key));
                        })
                        .toList();

                if (!data.isEmpty()) {
                    clientManager.getTransferDataClient().transfer(data);
                }
            } else if (selfNode.id().compareTo(id) == 0) { // Verifica se ID do nó atual é igual ao ID do nó que está entrando na rede.
                // Retorna erro.
                responseObserver.onError(
                        Status.INVALID_ARGUMENT
                                .withDescription("A node already exists for the ID informed.")
                                .asRuntimeException()
                );
            } else {
                // Encaminha a mensagem para o sucessor do nó atual.
                LOGGER.info("Forwarding new node to join the chord...");
                clientManager = ChordClientManager.withHost(
                        selfNode.getSuccessor().ip(),
                        selfNode.getSuccessor().port()
                );
                clientManager.getJoinChordClient().join(joinChordRequest.getNewNode());
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
        } finally {
            if (clientManager != null) {
                clientManager.closeClient();
            }
        }

    }
}
