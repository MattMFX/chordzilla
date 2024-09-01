package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

@Getter
public class ChordNode {
    private final String ip;
    private final Integer port;

    public ChordNode(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
