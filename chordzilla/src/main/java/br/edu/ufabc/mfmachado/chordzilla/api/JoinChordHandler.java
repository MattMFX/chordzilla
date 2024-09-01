package br.edu.ufabc.mfmachado.chordzilla.api;

import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServer;
import br.edu.ufabc.mfmachado.chordzilla.core.node.ChordNode;
import lombok.RequiredArgsConstructor;

import java.util.List;

@RequiredArgsConstructor
public class JoinChordHandler {

    private final GrpcServer grpcServer;

    public void join(String ip, Integer port) {
        try {
            System.out.println("Joining the network");
            grpcServer.start();
        } catch (Exception e) {
            System.out.println("Error joining the network");
        }

    }

    public void join(List<ChordNode> nodes) {
        try {
            System.out.println("Joining the network");
            grpcServer.start();
        } catch (Exception e) {
            System.out.println("Error joining the network");
        }

    }
}
