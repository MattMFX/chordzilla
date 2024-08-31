package br.edu.ufabc.mfmachado.chordzilla.core.hash;

public interface HashStrategy {
    Integer hash(String key);
    Integer hash(byte[] key);
}
