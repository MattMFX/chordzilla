package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import com.google.common.hash.Hashing;

import java.nio.charset.StandardCharsets;

public class SecureHash implements HashStrategy {
    @Override
    public Integer hash(String key) {
        return Hashing.sha256()
                .hashBytes(key.getBytes(StandardCharsets.UTF_8))
                .asInt();
    }

    @Override
    public Integer hash(byte[] key) {
        return Hashing.sha256()
                .hashBytes(key)
                .asInt();
    }
}
