package br.edu.ufabc.mfmachado.codehub.usecase;

import br.edu.ufabc.mfmachado.codehub.entity.Host;

import java.util.List;

public interface JoinChord {
    void join(String ip, int port);
    void join(List<Host> hosts);
}
