package br.edu.ufabc.mfmachado.chordzilla;

import br.edu.ufabc.mfmachado.chordzilla.api.StoreHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.ChordInitializerService;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.DummyHash;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.impl.SecureHash;

import java.math.BigInteger;
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
        HashMap<byte[], BigInteger> strMap = new HashMap<>();
        strMap.put("abc".getBytes(), new BigInteger("21000013"));
        strMap.put("bcd".getBytes(), new BigInteger("7123444234"));
        strMap.put("cde".getBytes(), new BigInteger("112374009"));
        DummyHash dummyHash = new DummyHash(strMap);

        SecureHash secureHash = new SecureHash();
        System.out.println("bytes aqye: " + secureHash.hash("aqye".getBytes()));
        System.out.println("bytes aaaa: " + secureHash.hash("aaaa".getBytes()));

        StoreHandler storeHandler = new StoreHandler();
        storeHandler.store();
    }
}
