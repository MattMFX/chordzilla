package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.api.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.HashMap;

public class App {
    public static void main(String[] args) {
//        ChordInitializerService chordInitializerService = new ChordInitializerService(new DummyHash());
//        try {
//            chordInitializerService.initialize();
//        } catch (Exception e) {
//            e.printStackTrace(); //TODO Improve error handling
//        }
        HashMap<byte[], byte[]> strMap = new HashMap<>();
        strMap.put("abc".getBytes(), "123".getBytes());
        strMap.put("bcd".getBytes(), "632234".getBytes());
        strMap.put("cde".getBytes(), "14562423".getBytes());
        DummyHash dummyHash = new DummyHash(strMap);

        SecureHash secureHash = new SecureHash();
        System.out.println("bytes aqye: " + Arrays.toString(secureHash.hash("aqye".getBytes())));
        System.out.println("bytes aaaa: " + Arrays.toString(secureHash.hash("aaaa".getBytes())));

        StoreHandler storeHandler = new StoreHandler();
        storeHandler.store();
    }
}
