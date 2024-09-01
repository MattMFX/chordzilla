package br.edu.ufabc.mfmachado.chordzilla.core.hash;

public interface HashStrategy {
    byte[] hash(byte[] key);
}
