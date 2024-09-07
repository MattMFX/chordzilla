package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SecureHash implements HashStrategy {

    @Override
    public BigInteger hash(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        byte[] bytes = Hashing.sha256()
                .hashBytes(key)
                .asBytes();

        return new BigInteger(1, bytes);
    }
}
