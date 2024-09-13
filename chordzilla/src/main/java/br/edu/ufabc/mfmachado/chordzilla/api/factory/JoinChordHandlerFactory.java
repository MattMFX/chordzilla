package br.edu.ufabc.mfmachado.chordzilla.api.factory;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashType;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Map;

public class JoinChordHandlerFactory {
    private HashType hashType;
    private Map<ByteBuffer, BigInteger> hashMap = Map.of();
    private String ip;
    private Integer port;

    public static JoinChordHandlerFactory newFactory() {
        return new JoinChordHandlerFactory();
    }

    public JoinChordHandlerFactory withIp(String ip) {
        this.ip = ip;
        return this;
    }

    public JoinChordHandlerFactory withPort(Integer port) {
        this.port = port;
        return this;
    }

    public JoinChordHandlerFactory withHashStrategy(HashType strategy) {
        this.hashType = strategy;
        return this;
    }

    public JoinChordHandlerFactory withMappings(Map<ByteBuffer, BigInteger> hashMap) {
        this.hashMap = hashMap;
        return this;
    }

    public JoinChordHandler create() {
        HashStrategy hashStrategy = switch (hashType) {
            case DUMMYHASH -> new DummyHash(hashMap);
            case SECUREHASH -> new SecureHash();
        };

        return new JoinChordHandler(
                new ChordInitializerService(hashStrategy, ip, port)
        );
    }
}
