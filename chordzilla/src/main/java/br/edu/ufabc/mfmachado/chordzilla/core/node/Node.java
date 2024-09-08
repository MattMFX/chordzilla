package br.edu.ufabc.mfmachado.chordzilla.core.node;

import java.math.BigInteger;

public interface Node {
    BigInteger id();
    String ip();
    Integer port();
}
