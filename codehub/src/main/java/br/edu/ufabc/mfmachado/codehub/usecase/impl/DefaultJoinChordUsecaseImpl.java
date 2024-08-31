package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.JoinChordHandler;
import br.edu.ufabc.mfmachado.codehub.usecase.JoinChordUsecase;

public class DefaultJoinChordUsecaseImpl implements JoinChordUsecase {
    public void join(String ip, int port) {
        System.out.println("Joining chord with ip " + ip + " and port " + port);
    }
}
