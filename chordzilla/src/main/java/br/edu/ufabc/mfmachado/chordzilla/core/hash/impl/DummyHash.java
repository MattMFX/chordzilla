package br.edu.ufabc.mfmachado.chordzilla.core.hash.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;

import java.util.HashMap;

public class DummyHash implements HashStrategy {
    private final HashMap<byte[], Integer> hashTable;

    public DummyHash() {
        this.hashTable = new HashMap<>();
    }

    @Override
    public Integer hash(String key) {
        return hash(key.getBytes());
    }

    @Override
    public Integer hash(byte[] key) {
        return hashTable.get(key);
    }
}
