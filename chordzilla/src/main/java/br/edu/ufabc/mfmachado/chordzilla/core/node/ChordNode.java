package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class ChordNode {
    private final BigInteger id;
    private final String ip;
    private final Integer port;

    public ChordNode(BigInteger id, String ip, Integer port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }
}
