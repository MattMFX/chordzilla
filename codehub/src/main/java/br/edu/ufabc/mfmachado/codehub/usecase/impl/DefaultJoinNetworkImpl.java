package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.factory.JoinChordHandlerFactory;
import br.edu.ufabc.mfmachado.chordzilla.api.handler.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.api.model.ChordHost;
import br.edu.ufabc.mfmachado.chordzilla.core.hash.HashType;
import br.edu.ufabc.mfmachado.codehub.entity.Host;
import br.edu.ufabc.mfmachado.codehub.usecase.JoinNetwork;

import java.util.List;

public class DefaultJoinNetworkImpl implements JoinNetwork {
    @Override
    public void newChord() {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .create();
        handler.newChord();
    }

    @Override
    public void newChord(String ip, int port) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .withIp(ip)
                .withPort(port)
                .create();
        handler.newChord();
    }

    @Override
    public void join(String targetIp, int targetPort) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .create();
        handler.join(targetIp, targetPort);
    }

    @Override
    public void join(String ip, int port, String targetIp, int targetPort) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .withIp(ip)
                .withPort(port)
                .create();
        handler.join(targetIp, targetPort);
    }

    @Override
    public void join(List<Host> hosts) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .create();
        handler.join(hosts.stream().map(host -> new ChordHost(host.ip(), host.port())).toList());
    }

    @Override
    public void join(String ip, int port, List<Host> hosts) {
        JoinChordHandler handler = JoinChordHandlerFactory.newFactory()
                .withHashStrategy(HashType.SECUREHASH)
                .withIp(ip)
                .withPort(port)
                .create();
        handler.join(hosts.stream().map(host -> new ChordHost(host.ip(), host.port())).toList());
    }
}
