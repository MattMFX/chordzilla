package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

@Getter
public class NeighborNode {
    private final String ip;
    private final Integer port;

    public NeighborNode(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }
}
