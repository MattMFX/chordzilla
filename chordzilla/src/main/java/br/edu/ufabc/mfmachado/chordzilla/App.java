package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.api.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.client.JoinChordClient;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;
import br.edu.ufabc.mfmachado.chordzilla.server.utils.NodeUtils;
import io.grpc.Channel;
import io.grpc.Grpc;
import io.grpc.InsecureChannelCredentials;

import java.io.Console;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;
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
        ChordInitializerService chordInitializerService = new ChordInitializerService(new DummyHash(hashes));

        try {
            chordInitializerService.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

        Console console = System.console();
        String cmd = console.readLine("Enter a command: ");

        if (cmd.equals("join")) {
            String id = console.readLine("Enter the id: ");
            String ip = console.readLine("Enter the ip: ");
            String port = console.readLine("Enter the port: ");
            String targetIp = console.readLine("Enter the target ip: ");
            String targetPort = console.readLine("Enter the target port: ");

            Channel channel = Grpc.newChannelBuilder(NodeUtils.getNodeAdress(targetIp, Integer.parseInt(targetPort)), InsecureChannelCredentials.create()).build();
            JoinChordClient joinChordClient = new JoinChordClient(channel);
            joinChordClient.join(NodeInformation.newBuilder()
                    .setId(id)
                    .setIp(ip)
                    .setPort(Integer.parseInt(port))
                    .build()
            );
        }
    }
}
