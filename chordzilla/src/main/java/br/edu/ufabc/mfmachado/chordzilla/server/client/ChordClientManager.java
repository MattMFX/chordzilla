package br.edu.ufabc.mfmachado.chordzilla.server.client;

import br.edu.ufabc.mfmachado.chordzilla.proto.NotifyPredecessorNodeGoneResponse;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.*;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.util.Map;

public class ChordClientManager {
    private final ManagedChannel channel;

    private ChordClientManager(ManagedChannel channel) {
        this.channel = channel;
    }

    public static ChordClientManager withHost(String ip, Integer port) {
        ManagedChannel channel = Grpc
                .newChannelBuilder(NodeUtils.getNodeAdress(ip, port), InsecureChannelCredentials.create())
                .maxInboundMessageSize(1024 * 1024 * 1024)
                .build();
        return new ChordClientManager(channel);
    }

    public AcknowledgeJoinClient getAcknowledgeJoinClient() {
        return new AcknowledgeJoinClient(channel);
    }

    public DisplayChordClient getDisplayChordClient() {
        return new DisplayChordClient(channel);
    }

    public JoinChordClient getJoinChordClient() {
        return new JoinChordClient(channel);
    }

    public LeaveChordClient getLeaveChordClient() {
        return new LeaveChordClient(channel);
    }

    public NotifyPredecessorNewNodeClient getNotifyPredecessorNewNodeClient() {
        return new NotifyPredecessorNewNodeClient(channel);
    }

    public NotifyPredecessorNodeGoneClient getNotifyPredecessorNodeGoneClient() {
        return new NotifyPredecessorNodeGoneClient(channel);
    }

    public RetrieveDataClient getRetrieveDataClient() {
        return new RetrieveDataClient(channel);
    }

    public RetrieveDataNotFoundClient getRetrieveDataNotFoundClient() {
        return new RetrieveDataNotFoundClient(channel);
    }

    public RetrieveDataOkClient getRetrieveDataOkClient() {
        return new RetrieveDataOkClient(channel);
    }

    public StoreDataClient getStoreDataClient() {
        return new StoreDataClient(channel);
    }

    public TransferDataClient getTransferDataClient() {
        return new TransferDataClient(channel);
    }

    public void closeClient() {
        channel.shutdown();
    }
}
