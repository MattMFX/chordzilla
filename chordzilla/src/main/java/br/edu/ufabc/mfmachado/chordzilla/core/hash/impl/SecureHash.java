package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import com.google.common.hash.Hashing;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;

public class SecureHash implements HashStrategy {

    @Override
    public byte[] hash(byte[] key) {
        if (key == null) {
            throw new IllegalArgumentException("Key cannot be null");
        }

        return Hashing.sha256()
                .hashBytes(key)
                .asBytes();
    }
}
