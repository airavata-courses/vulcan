package com.WeatherForcast.DbMgmtService.Utility;

import com.WeatherForcast.DbMgmtService.model.NexradModel;
import com.WeatherForcast.DbMgmtService.grpc.msgUrl;
import org.springframework.stereotype.Service;

@Service
public class Utility {
    public NexradModel convertToUrlModel(msgUrl model){

        String url = model.getUrl();
        String date = model.getDate();
        String month = model.getMonth();
        String year = model.getYear();
        String station = model.getStation();
        String time = model.getTime();

        return new NexradModel(
                date,
                month,
                year,
                station,
                time,
                url);

    }

    public msgUrl convertToMsgUrl(NexradModel model){

        var builder = msgUrl.newBuilder();

        builder.setDate(model.getDate());
        builder.setMonth(model.getMonth());
        builder.setYear(model.getYear());
        builder.setStation(model.getStation());
        builder.setTime(model.getTime());
        builder.setUrl(model.getUrl());

        return builder.build();
    }
}
