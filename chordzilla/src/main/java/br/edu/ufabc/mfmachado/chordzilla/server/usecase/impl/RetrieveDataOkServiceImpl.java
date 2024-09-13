package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.api.Client;
import br.edu.ufabc.mfmachado.chordzilla.api.model.Response;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.RetrieveDataOkResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class RetrieveDataOkServiceImpl extends RetrieveDataOkGrpc.RetrieveDataOkImplBase {
    private final Logger LOGGER = LoggerFactory.getLogger(RetrieveDataOkServiceImpl.class);

    @Override
    public void ok(RetrieveDataOkRequest request, StreamObserver<RetrieveDataOkResponse> responseObserver) {
        try {
            LOGGER.info("Received data retrieval response.");
            BigInteger key = new BigInteger(request.getData().getKey());

            Client.getFuture(key).complete(new Response<>(Response.StatusCode.OK, request.getData()));
            LOGGER.info("Data retrieved successfully with key {}.", key);

            responseObserver.onNext(RetrieveDataOkResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving data: {}", e.getMessage(), e);
            BigInteger key = new BigInteger(request.getData().getKey());
            Client.getFuture(key).complete(new Response<>(Response.StatusCode.INTERNAL_SERVER_ERROR, null));
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while receiving data.")
                            .asRuntimeException()
            );
        }
    }
}
