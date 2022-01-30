package com.WeatherForcast.DbMgmtService.controller;

import com.WeatherForcast.DbMgmtService.grpc.DbMgmtServiceGrpc;
import com.WeatherForcast.DbMgmtService.grpc.msgId;
import com.WeatherForcast.DbMgmtService.grpc.msgUrl;
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
    public void saveurl(msgUrl request, StreamObserver<msgId> responseObserver) {
        String url = request.getUrl();
        int id = this.service.saveUrl(new UrlModel(url)).getId();

        msgId response = msgId.newBuilder().setId(id).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void geturl(msgId request, StreamObserver<msgUrl> responseObserver) {
        Integer id =  request.getId();
        String url = this.service.getUrl(id);

        msgUrl response = msgUrl.newBuilder().setUrl(url).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
