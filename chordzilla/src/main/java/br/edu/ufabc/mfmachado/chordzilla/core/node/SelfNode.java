package br.edu.ufabc.mfmachado.chordzilla.core.node;

import lombok.Getter;

import java.math.BigInteger;
import java.util.Objects;

public class SelfNode implements Node {
    private final BigInteger id = InternalNode.id;
    private final String ip = InternalNode.ip;
    private final Integer port = InternalNode.port;
    private Node successor = InternalNode.successor;
    private Node predecessor = InternalNode.predecessor;
    
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
        if (Objects.isNull(this.predecessor) && Objects.isNull(this.successor)) {
            return true;
        }

        boolean predecessorIdIsLessThanId = this.predecessor.id().compareTo(this.id) < 0;
        boolean keyIsGreaterThanId = key.compareTo(this.id) >= 0;
        boolean selfIsFirstNode = key.compareTo(this.predecessor.id()) < 0;


        return (keyIsGreaterThanId && predecessorIdIsLessThanId)
                || (selfIsFirstNode && (keyIsGreaterThanId || predecessorIdIsLessThanId));
    }

    @Override
    public BigInteger id() {
        return this.id;
    }

    @Override
    public String ip() {
        return this.ip;
    }

    @Override
    public Integer port() {
        return this.port;
    }

    public Node getPredecessor() {
        if (Objects.nonNull(InternalNode.predecessor) && !InternalNode.predecessor.equals(this.predecessor)) {
            this.predecessor = InternalNode.predecessor;
        }
        return this.predecessor;
    }

    public Node getSuccessor() {
        if (Objects.nonNull(InternalNode.successor) && !InternalNode.successor.equals(this.successor)) {
            this.successor = InternalNode.successor;
        }
        return this.successor;
    }

    public void setPredecessor(Node predecessor) {
        InternalNode.predecessor = predecessor;
    }

    public void setSuccessor(Node successor) {
        InternalNode.successor = successor;
    }

    private static class InternalNode implements Node {
        private static BigInteger id;
        private static String ip;
        private static Integer port;
        private static Node successor;
        private static Node predecessor;

        @Override
        public BigInteger id() {
            return id;
        }

        @Override
        public String ip() {
            return ip;
        }

        @Override
        public Integer port() {
            return port;
        }
    }
}


