package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;

public class App {
    public static void main(String[] args) {
        ChordInitializerService chordInitializerService = new ChordInitializerService(new DummyHash());
        try {
            chordInitializerService.initialize();
        } catch (Exception e) {
            e.printStackTrace(); //TODO Improve error handling
        }
    }
}
