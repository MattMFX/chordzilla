package br.edu.ufabc.mfmachado.chordzilla.api.entrypoint;

import br.edu.ufabc.mfmachado.chordzilla.core.GrpcServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class LeaveHandler {

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
