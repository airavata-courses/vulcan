package com.WeatherForcast.DbMgmtService.controller;

import com.WeatherForcast.DbMgmtService.Utility.Utility;
import com.WeatherForcast.DbMgmtService.grpc.DbMgmtServiceGrpc;
import com.WeatherForcast.DbMgmtService.grpc.msgId;
import com.WeatherForcast.DbMgmtService.grpc.msgUrl;
import com.WeatherForcast.DbMgmtService.model.NexradModel;
import com.WeatherForcast.DbMgmtService.service.DbMgmtService;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.springframework.beans.factory.annotation.Autowired;

@GrpcService
public class DbMgmtController extends DbMgmtServiceGrpc.DbMgmtServiceImplBase {

    @Autowired
    private DbMgmtService service;

    @Autowired
    private Utility utility;

    @Override
    public void saveurl(msgUrl request, StreamObserver<msgId> responseObserver) {

        NexradModel model = utility.convertToUrlModel(request);

        int id = this.service.saveUrl(model).getId();

            msgId response = msgId.newBuilder().setId(id).build();

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void geturl(msgId request, StreamObserver<msgUrl> responseObserver) {
        Integer id =  request.getId();
        NexradModel model = this.service.getUrl(id);

        msgUrl response = this.utility.convertToMsgUrl(model);

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
