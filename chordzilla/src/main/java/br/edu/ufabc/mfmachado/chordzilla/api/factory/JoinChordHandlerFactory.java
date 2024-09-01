package br.edu.ufabc.mfmachado.chordzilla.api.factory;

import br.edu.ufabc.mfmachado.chordzilla.api.JoinChordHandler;
import br.edu.ufabc.mfmachado.chordzilla.core.grcp.GrpcServerFactory;

public class JoinChordHandlerFactory {
    public static JoinChordHandlerFactory newFactory() {
        return new JoinChordHandlerFactory();
    }

    public JoinChordHandler create() {
        return new JoinChordHandler(
                GrpcServerFactory.newFactory().create()
        );
    }
}
