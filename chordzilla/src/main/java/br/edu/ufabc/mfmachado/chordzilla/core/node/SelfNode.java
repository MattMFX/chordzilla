package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

@Getter
public class SelfNode {
    private byte[] id;
    private String ip;
    private Integer port;
    private ChordNode successor;
    private ChordNode predecessor;

    public SelfNode(byte[] id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }
}
