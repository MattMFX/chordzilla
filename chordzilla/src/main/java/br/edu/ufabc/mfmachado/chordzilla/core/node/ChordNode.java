package br.edu.ufabc.mfmachado.chordzilla.core.node;

import java.math.BigInteger;

public record ChordNode(BigInteger id, String ip, Integer port) implements Node {
}
