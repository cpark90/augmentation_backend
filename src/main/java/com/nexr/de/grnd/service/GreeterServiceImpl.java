package com.nexr.de.grnd.service;

import io.grpc.stub.StreamObserver;
import com.nexr.de.grnd.proto.GreeterServiceGrpc;
import com.nexr.de.grnd.proto.GreeterRequest;
import com.nexr.de.grnd.proto.GreeterResponse;

public class GreeterServiceImpl extends GreeterServiceGrpc.GreeterServiceImplBase {

    @Override
    public void greet(GreeterRequest request, StreamObserver<GreeterResponse> responseObserver) {
        String name = request.getName();
        String message = "Hello, " + name + "!";

        GreeterResponse response = GreeterResponse.newBuilder()
                .setMessage(message)
                .build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}