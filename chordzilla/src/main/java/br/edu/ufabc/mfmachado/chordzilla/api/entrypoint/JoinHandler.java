package br.edu.ufabc.mfmachado.chordzilla.api.entrypoint;

import br.edu.ufabc.mfmachado.chordzilla.core.GrpcServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JoinHandler {

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
