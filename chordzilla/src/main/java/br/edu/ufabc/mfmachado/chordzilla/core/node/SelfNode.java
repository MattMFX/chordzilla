package br.edu.ufabc.mfmachado.chordzilla.core.node;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.*;

public class SelfNode implements Node {
    private static final Logger LOGGER = LoggerFactory.getLogger(SelfNode.class);

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
            LOGGER.info("Node already initialized. Returning existing instance.");
            return getInstance();
        }

        InternalNode.data = new HashMap<>();
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

        boolean selfIsFirstNode = this.id.compareTo(this.predecessor.id()) < 0;
        boolean idIsGreaterThanKey = this.id.compareTo(key) >= 0;
        boolean predecessorIdIsLessThanKey = this.predecessor.id().compareTo(key) < 0;

        return (idIsGreaterThanKey && predecessorIdIsLessThanKey)
                || (selfIsFirstNode && (idIsGreaterThanKey || predecessorIdIsLessThanKey));
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
        this.predecessor = InternalNode.predecessor;
    }

    public void setSuccessor(Node successor) {
        InternalNode.successor = successor;
        this.successor = InternalNode.successor;
    }

    public void addData(BigInteger key, byte[] value) {
        InternalNode.data.put(key, value);
    }

    public byte[] popData(BigInteger key) {
        return InternalNode.data.remove(key);
    }

    public List<BigInteger> getKeys() {
        return new ArrayList<>(InternalNode.data.keySet());
    }

    private static class InternalNode implements Node {
        private static Map<BigInteger, byte[]> data;
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


