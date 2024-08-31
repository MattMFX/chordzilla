package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JoinChordHandler {

    private final GrpcServer grpcServer;

    public void join() {
        try {
            System.out.println("Joining the network");
            grpcServer.start();
        } catch (Exception e) {
            System.out.println("Error joining the network");
        }

    }
}
