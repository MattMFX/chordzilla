package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.api.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.LeaveChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.factory.JoinChordHandlerFactory;
import br.edu.ufabc.mfmachado.chordzilla.client.*;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashType;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;

import java.io.Console;
import java.math.BigInteger;
import java.util.Map;

public class App {
    public static void main(String[] args) {
        Map<byte[], BigInteger> hashes = Map.of(
                "archive1".getBytes(), new BigInteger("1"),
                "archive2".getBytes(), new BigInteger("2"),
                "archive3".getBytes(), new BigInteger("3"),
                "archive4".getBytes(), new BigInteger("4"),
                "archive5".getBytes(), new BigInteger("5")
        );

        Console console = System.console();
        while (true) {
            String cmd = console.readLine("Enter a command: ");
            if (cmd.equals("join")) {
                String targetIp = console.readLine("Enter the target ip: ");
                String targetPort = console.readLine("Enter the target port: ");

                JoinChordHandler joinChordHandler = JoinChordHandlerFactory.newFactory()
                        .withHashStrategy(HashType.DUMMYHASH)
                        .withMappings(hashes)
                        .create();
                joinChordHandler.join(targetIp, Integer.parseInt(targetPort));
            } else if (cmd.equals("display")) {
                SelfNode selfNode = SelfNode.getInstance();
                Channel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create()).build();
                DisplayChordClient client = new DisplayChordClient(channel);
                client.display();
            } else if (cmd.equals("leave")) {
                LeaveChordHandler leaveChordHandler = new LeaveChordHandler();
                leaveChordHandler.leave();
            } else if (cmd.equals("store")) {
                String key = console.readLine("Enter the key: ");
                String value = console.readLine("Enter the value: ");
                StoreHandler storeHandler = new StoreHandler();
                storeHandler.store(key, value.getBytes());
            }
        }

    }
}
