package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import lombok.Getter;

public class Client {
    @Getter
    private static HashStrategy hashStrategy;

    public static void setHashStrategy(HashStrategy hashStrategy) {
        Client.hashStrategy = hashStrategy;
    }
}
