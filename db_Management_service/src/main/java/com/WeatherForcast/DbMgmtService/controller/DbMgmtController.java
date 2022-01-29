package com.WeatherForcast.DbMgmtService.controller;

import com.WeatherForcast.DbMgmtService.grpc.DbMgmtServiceGrpc;
import com.WeatherForcast.DbMgmtService.grpc.UrlRequest;
import com.WeatherForcast.DbMgmtService.grpc.UrlResponse;
import com.WeatherForcast.DbMgmtService.model.UrlModel;
import com.WeatherForcast.DbMgmtService.service.DbMgmtService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class DbMgmtController extends DbMgmtServiceGrpc.DbMgmtServiceImplBase {

    @Autowired
    private DbMgmtService service;

    @Override
    public void saveurl(UrlRequest request, StreamObserver<UrlResponse> responseObserver) {
        String url = request.getUrl();
        int id = this.service.saveUrl(new UrlModel(url)).getId();

        UrlResponse response = UrlResponse.newBuilder().setId(id).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
