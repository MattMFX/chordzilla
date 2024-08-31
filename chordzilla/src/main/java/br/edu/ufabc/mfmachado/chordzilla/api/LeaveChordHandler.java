package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LeaveChordHandler {

    private final GrpcServer grpcServer;

    public void leave() {
        try {
            System.out.println("Leaving the network");
            grpcServer.stop();
        } catch (Exception e) {
            System.out.println("Error leaving the network");
        }
    }
}
