package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

import java.math.BigInteger;

@Getter
public class SelfNode {
    private final BigInteger id = InternalNode.id;
    private final String ip = InternalNode.ip;
    private final Integer port = InternalNode.port;
    private final ChordNode successor = InternalNode.successor;
    private final ChordNode predecessor = InternalNode.predecessor;
    
    private SelfNode() {
    }

    public static SelfNode getInstance() {
        return new SelfNode();
    }

    public static SelfNode init(BigInteger id, String ip, Integer port) {
        if (InternalNode.id != null || InternalNode.ip != null || InternalNode.port != null) {
            throw new IllegalStateException("Node already initialized");
        }

        InternalNode.id = id;
        InternalNode.ip = ip;
        InternalNode.port = port;
        return new SelfNode();
    }

    /**
     * Verifica se o ID do nó que está entrando na rede está entre o ID do nó atual e o ID do nó predecessor.
     * Caso 1: O ID do nó que está entrando na rede é menor que o ID do nó atual e maior que o ID do nó predecessor.
     * Caso 2: O nó atual é o menor ID da rede, então seu predecessor tem ID maior que o nó atual.
     * 1a: O ID do nó que está entrando na rede é menor que o ID do nó atual.
     * 1b: O ID do nó que está entrando na rede é maior que o ID do nó predecessor.
     */
    public Boolean isOwner(BigInteger key) {
        boolean predecessorIdIsLessThanId = this.predecessor.getId().compareTo(this.id) < 0;
        boolean keyIsGreaterThanId = key.compareTo(this.id) >= 0;
        boolean selfIsFirstNode = key.compareTo(this.predecessor.getId()) < 0;


        return (keyIsGreaterThanId && predecessorIdIsLessThanId)
                || (selfIsFirstNode && (keyIsGreaterThanId || predecessorIdIsLessThanId));
    }

    private static class InternalNode {
        private static BigInteger id;
        private static String ip;
        private static Integer port;
        private static ChordNode successor;
        private static ChordNode predecessor;
    }
}


