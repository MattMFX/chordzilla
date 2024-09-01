package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class DummyHash implements HashStrategy {
    private static BigInteger incrementalId = BigInteger.ZERO;
    private final Map<byte[], byte[]> map;

    public DummyHash(Map<byte[], byte[]> hashMap) {
        this.map = hashMap;
    }

    @Override
    public byte[] hash(byte[] key) {
        if (map.containsKey(key)) {
            return map.get(key);
        }

        incrementalId = incrementalId.add(BigInteger.ONE);
        return incrementalId.toString().getBytes();
    }
}
