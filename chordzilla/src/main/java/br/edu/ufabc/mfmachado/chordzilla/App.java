package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.handler.LeaveChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.handler.RetrieveHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.handler.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.factory.JoinChordHandlerFactory;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashType;
import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.server.client.impl.DisplayChordClient;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;
import io.grpc.ManagedChannel;

import java.io.Console;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;
import java.util.Optional;

public class App {
    public static void main(String[] args) throws InterruptedException {
        Map<ByteBuffer, BigInteger> hashes = new java.util.HashMap<>(Map.of(
                ByteBuffer.wrap("127.0.0.1:10000".getBytes()), new BigInteger("10000"),
                ByteBuffer.wrap("127.0.0.1:15000".getBytes()), new BigInteger("15000"),
                ByteBuffer.wrap("127.0.0.1:20000".getBytes()), new BigInteger("20000"),
                ByteBuffer.wrap("127.0.0.1:25000".getBytes()), new BigInteger("25000"),
                ByteBuffer.wrap("127.0.0.1:30000".getBytes()), new BigInteger("30000"),
                ByteBuffer.wrap("127.0.0.1:35000".getBytes()), new BigInteger("35000"),
                ByteBuffer.wrap("127.0.0.1:40000".getBytes()), new BigInteger("40000"),
                ByteBuffer.wrap("127.0.0.1:45000".getBytes()), new BigInteger("45000"),
                ByteBuffer.wrap("127.0.0.1:50000".getBytes()), new BigInteger("50000"),
                ByteBuffer.wrap("127.0.0.1:55000".getBytes()), new BigInteger("55000")
        ));

        hashes.putAll(Map.of(
                ByteBuffer.wrap("archive1".getBytes()), new BigInteger("1"),
                ByteBuffer.wrap("archive2".getBytes()), new BigInteger("2"),
                ByteBuffer.wrap("archive3".getBytes()), new BigInteger("3"),
                ByteBuffer.wrap("archive4".getBytes()), new BigInteger("4"),
                ByteBuffer.wrap("archive5".getBytes()), new BigInteger("5")
        ));

        Console console = System.console();
        while (true) {
            Thread.sleep(1000);
            String cmd = console.readLine("Enter a command: ");
            if (cmd.equals("new")) {
                String ip = console.readLine("Enter the ip: ");
                String port = console.readLine("Enter the port: ");
                JoinChordHandler joinChordHandler = JoinChordHandlerFactory.newFactory()
                        .withHashStrategy(HashType.SECUREHASH)
                        .withMappings(hashes)
                        .withIp(ip)
                        .withPort(Integer.parseInt(port))
                        .create();
                joinChordHandler.newChord();
            } else if (cmd.equals("join")) {
                String ip = console.readLine("Enter the ip: ");
                String port = console.readLine("Enter the port: ");
                String targetIp = console.readLine("Enter the target ip: ");
                String targetPort = console.readLine("Enter the target port: ");
                JoinChordHandler joinChordHandler = JoinChordHandlerFactory.newFactory()
                        .withHashStrategy(HashType.SECUREHASH)
                        .withMappings(hashes)
                        .withIp(ip)
                        .withPort(Integer.parseInt(port))
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
                SelfNode selfNode = SelfNode.getInstance();
                ManagedChannel channel = Grpc
                        .newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create())
                        .build();
                StoreHandler storeHandler = new StoreHandler(channel);
                storeHandler.store(key, value.getBytes());
            } else if (cmd.equals("retrieve")) {
                String key = console.readLine("Enter the key: ");
                SelfNode selfNode = SelfNode.getInstance();
                ManagedChannel channel = Grpc
                        .newChannelBuilder(NodeUtils.getNodeAdress(selfNode.ip(), selfNode.port()), InsecureChannelCredentials.create())
                        .build();
                RetrieveHandler retrieveDataHandler = new RetrieveHandler(channel);
                Optional<byte[]> dataOptional = retrieveDataHandler.retrieve(key);
                if (dataOptional.isPresent()) {
                    System.out.println("Data: " + new String(dataOptional.get()));
                } else {
                    System.out.println("Data not found");
                }
            } else if (cmd.equals("mock")) {
                String key = console.readLine("Enter the key: ");
                String value = console.readLine("Enter the mocked key: ");
                hashes.put(ByteBuffer.wrap(key.getBytes()), new BigInteger(value));
            }
        }

    }
}
