package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.factory.JoinChordHandlerFactory;
import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.usecase.JoinChord;

import java.util.List;

public class DefaultJoinChordImpl implements JoinChord {
    @Override
    public void join(String ip, int port) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory().create();
        handler.join();
    }

    @Override
    public void join(List<Host> hosts) {
        System.out.println("Joining chord with hosts " + hosts);
    }
}
