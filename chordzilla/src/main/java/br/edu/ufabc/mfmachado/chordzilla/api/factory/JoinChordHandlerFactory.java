package br.edu.ufabc.mfmachado.chordzilla.api.factory;

import br.edu.ufabc.mfmachado.chordzilla.api.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashType;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;

import java.math.BigInteger;
import java.util.Map;

public class JoinChordHandlerFactory {
    private HashType hashType;
    private Map<byte[], BigInteger> hashMap;

    public static JoinChordHandlerFactory newFactory() {
        return new JoinChordHandlerFactory();
    }

    public JoinChordHandlerFactory withHashStrategy(HashType strategy) {
        this.hashType = strategy;
        return this;
    }

    public JoinChordHandlerFactory withMappings(Map<byte[], BigInteger> hashMap) {
        this.hashMap = hashMap;
        return this;
    }

    public JoinChordHandler create() {
        HashStrategy hashStrategy = switch (hashType) {
            case DUMMYHASH -> new DummyHash(hashMap);
            case SECUREHASH -> new SecureHash();
        };

        return new JoinChordHandler(
                new ChordInitializerService(hashStrategy)
        );
    }
}
