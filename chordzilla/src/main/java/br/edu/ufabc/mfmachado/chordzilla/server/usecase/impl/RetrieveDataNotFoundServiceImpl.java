package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.api.model.Response;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataNotFoundResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class RetrieveDataNotFoundServiceImpl extends RetrieveDataNotFoundGrpc.RetrieveDataNotFoundImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(RetrieveDataNotFoundServiceImpl.class);

    @Override
    public void notFound(RetrieveDataNotFoundRequest request, StreamObserver<RetrieveDataNotFoundResponse> responseObserver) {

        try {
            LOGGER.info("Received data not found response for ID {}.", request.getKey());
            BigInteger key = new BigInteger(request.getKey());
            Client.getFuture(key).complete(new Response<>(Response.StatusCode.NOT_FOUND, null));
            responseObserver.onNext(RetrieveDataNotFoundResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving data: {}", e.getMessage(), e);
            BigInteger key = new BigInteger(request.getKey());
            Client.getFuture(key).complete(new Response<>(Response.StatusCode.INTERNAL_SERVER_ERROR, null));
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while receiving data.")
                            .asRuntimeException()
            );
        }
    }
}
