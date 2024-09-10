package br.edu.ufabc.mfmachado.chordzilla.server.utils;

import br.edu.ufabc.mfmachado.chordzilla.core.node.ChordNode;
import br.edu.ufabc.mfmachado.chordzilla.core.node.Node;
import br.edu.ufabc.mfmachado.chordzilla.proto.NodeInformation;

import java.math.BigInteger;

public class NodeUtils {

    public static String getNodeAdress(String ip, Integer port) {
        return ip + ":" + port;
    }

    public static NodeInformation mapToNodeInformation(Node nodeInformation) {
        return NodeInformation.newBuilder()
                .setId(nodeInformation.id().toString())
                .setIp(nodeInformation.ip())
                .setPort(nodeInformation.port())
                .build();
    }

    public static ChordNode mapToNode(NodeInformation nodeInformation) {
        return new ChordNode(
                new BigInteger(nodeInformation.getId()),
                nodeInformation.getIp(),
                nodeInformation.getPort()
        );
    }
}
