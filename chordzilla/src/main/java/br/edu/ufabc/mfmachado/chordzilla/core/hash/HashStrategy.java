package br.edu.ufabc.mfmachado.chordzilla.core.hash;

import java.math.BigInteger;

public interface HashStrategy {
    BigInteger hash(byte[] key);
}
