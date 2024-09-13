package br.edu.ufabc.mfmachado.codehub.usecase;

import br.edu.ufabc.mfmachado.codehub.entity.Host;

import java.util.List;

public interface JoinNetwork {
    void newChord();
    void newChord(String targetIp, int targetPort);
    void join(String targetIp, int targetPort);
    void join(String ip, int port, String targetIp, int targetPort);
    void join(List<Host> hosts);
    void join(String ip, int port, List<Host> hosts);
}
