package com.WeatherForcast.forecast_service.controller;
import com.WeatherForcast.forecast_service.configuration.Producer;
import com.WeatherForcast.forecast_service.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
@RequestMapping(value = "/api/forecast")
public class forecastController {

    private final Producer producer;

    @Autowired
    public forecastController(Producer producer) {
        this.producer = producer;
    }

    //rest based communication
    @PostMapping
    @RequestMapping("/predict")
    public String forecastPredict(@RequestBody String request){
        Type modelType = new TypeToken<List<ArrayList<ArrayList<Double>>>>() {}.getType();
        Gson gson = new Gson();
        List<ArrayList<ArrayList<Double>>> model = gson.fromJson(request, modelType);

        forecast _forecast = new forecast(
                "FeatureCollection", new ArrayList<>()
        {
            {
                add(new feature(
                        "Feature",
                        new properties(),
                        new geometry("Polygon", model)));
            }
        });


        System.out.println(String.format("Data sent to gateway:{%s}", gson.toJson(_forecast)));

        return gson.toJson(_forecast);
    }


    //kafka-based communication
//    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
//    public void forecast(ConsumerRecord<String, String> payload){
//
//        System.out.println(String.format("Data consumed:{%s}", payload.value()));
//
//        //mocking dummy value for the intensity
//        Type modelType = new TypeToken<List<ArrayList<ArrayList<Double>>>>() {}.getType();
//        Gson gson = new Gson();
//        List<ArrayList<ArrayList<Double>>> model = gson.fromJson(payload.value(), modelType);
//
//        forecast _forecast = new forecast(
//                "FeatureCollection", new ArrayList<>()
//        {
//            {
//                add(new feature(
//                        "Feature",
//                        new properties(),
//                        new geometry("Polygon", model)));
//            }
//        });
//
//
//        System.out.println(String.format("Data sent to gateway:{%s}", gson.toJson(_forecast)));
//        producer.send(gson.toJson(_forecast));
//
////        return gson.toJson(model);
//    }
}
