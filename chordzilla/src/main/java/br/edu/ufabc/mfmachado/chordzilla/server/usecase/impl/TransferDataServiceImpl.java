package br.edu.ufabc.mfmachado.chordzilla.server.usecase.impl;

import br.edu.ufabc.mfmachado.chordzilla.core.node.SelfNode;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataGrpc;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataRequest;
import br.edu.ufabc.mfmachado.chordzilla.proto.TransferDataResponse;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;

public class TransferDataServiceImpl extends TransferDataGrpc.TransferDataImplBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(TransferDataServiceImpl.class);

    @Override
    public void transfer(TransferDataRequest request, StreamObserver<TransferDataResponse> responseObserver) {
        try {
            LOGGER.info("Received data transfer request");

            SelfNode selfNode = SelfNode.getInstance();
            request.getDataList().forEach(data -> selfNode.addData(new BigInteger(data.getKey()), data.getValue().toByteArray()));

            responseObserver.onNext(TransferDataResponse.newBuilder().build());
            responseObserver.onCompleted();
        } catch (Exception e) {
            LOGGER.error("An error ocurred while receiving data transfer: {}", e.getMessage(), e);
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("An error ocurred while receiving data transfer.")
                            .asRuntimeException()
            );
        }
    }
}
