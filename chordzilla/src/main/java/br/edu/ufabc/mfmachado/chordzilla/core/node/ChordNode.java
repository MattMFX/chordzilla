package br.edu.ufabc.mfmachado.chordzilla.core.node;

public class ChordNode {
    private static Integer id;
    private static String ip;
    private static Integer port;
    private static NeighborNode successor;
    private static NeighborNode predecessor;

    private ChordNode() {}

    public static void init(Integer id, String ip, Integer port) {
        ChordNode.id = id;
        ChordNode.ip = ip;
        ChordNode.port = port;
    }
}
