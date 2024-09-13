package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.api.model.Response;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashStrategy;
import br.edu.ufabc.mfmachado.chordzilla.proto.Data;
import lombok.Getter;

import java.math.BigInteger;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

public class Client {
    @Getter
    private static HashStrategy hashStrategy;
    private static final Map<BigInteger, CompletableFuture<Response<Data>>> pendingRequests = new ConcurrentHashMap<>();

    public static void setHashStrategy(HashStrategy hashStrategy) {
        Client.hashStrategy = hashStrategy;
    }

    public static void addPendingRequest(BigInteger id, CompletableFuture<Response<Data>> future) {
        pendingRequests.put(id, future);
    }

    public static CompletableFuture<Response<Data>> getFuture(BigInteger id) {
        return pendingRequests.get(id);
    }

    public static Response<Data> getCompleted(BigInteger id) {
        try {
            return pendingRequests.get(id).get();
        } catch (Exception e) {
            throw new RuntimeException("Error getting completed request", e);
        }
    }
}
