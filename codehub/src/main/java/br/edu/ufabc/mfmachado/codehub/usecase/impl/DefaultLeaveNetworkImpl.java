package br.edu.ufabc.mfmachado.codehub.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.handler.LeaveChordHandler;
import br.edu.ufabc.mfmachado.codehub.usecase.LeaveNetwork;

public class DefaultLeaveNetworkImpl implements LeaveNetwork {
    @Override
    public void leave() {
        LeaveChordHandler leaveChordHandler = new LeaveChordHandler();
        leaveChordHandler.leave();
    }
}
