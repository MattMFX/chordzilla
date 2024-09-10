package br.edu.ufabc.mfmachado.chordzilla.server.utils;

import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import com.google.protobuf.ByteString;

import java.math.BigInteger;

public class DataUtils {
    public static Data mapToData(BigInteger key, byte[] value) {
        return Data.newBuilder()
                .setKey(key.toString())
                .setValue(ByteString.copyFrom(value))
                .build();
    }
}
