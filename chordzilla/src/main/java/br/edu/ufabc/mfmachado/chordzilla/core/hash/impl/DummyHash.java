package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class DummyHash implements HashStrategy {
    private final Map<ByteBuffer, BigInteger> map;

    public DummyHash(Map<ByteBuffer, BigInteger> hashMap) {
        this.map = hashMap;
    }

    @Override
    public BigInteger hash(byte[] key) {
        if (map.containsKey(ByteBuffer.wrap(key))) {
            return map.get(ByteBuffer.wrap(key));
        }

        return new BigInteger(String.valueOf(new Random().nextInt(1000)));
    }
}
