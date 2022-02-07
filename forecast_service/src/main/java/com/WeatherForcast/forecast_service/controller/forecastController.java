package com.WeatherForcast.forecast_service.controller;
import com.WeatherForcast.forecast_service.configuration.Producer;
import com.WeatherForcast.forecast_service.model.*;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
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
//@RequestMapping(value = "/api/forecast")
public class forecastController {

    private final Producer producer;

    @Autowired
    public forecastController(Producer producer) {
        this.producer = producer;
    }

//    @PostMapping
//    @RequestMapping("/test")
//    public String test(@RequestBody String payload){
//        Type modelType = new TypeToken<List<ArrayList<ArrayList<Double>>>>() {}.getType();
//        Gson gson = new Gson();
//        List<ArrayList<ArrayList<Double>>> model = gson.fromJson(payload, modelType);
//
//        forecast _forecast = new forecast(
//                "FeatureCollection", new ArrayList<feature>()
//        {
//            {
//                add(new feature(
//                        "Feature",
//                        new properties(),
//                        new geometry("Polygon", model)));
//            }
//        });
//
////        producer.send(gson.toJson(model));
//
//        return gson.toJson(_forecast);
//    }

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public void forecast(ConsumerRecord<String, String> payload){

        System.out.println(String.format("Data consumed:{%s}", payload.value()));

        //mocking dummy value for the intensity
        Type modelType = new TypeToken<List<ArrayList<ArrayList<Double>>>>() {}.getType();
        Gson gson = new Gson();
        List<ArrayList<ArrayList<Double>>> model = gson.fromJson(payload.value(), modelType);

//        for(forecastModel m: model){
//            m.setIntensity(ThreadLocalRandom.current()
//                    .nextFloat((float)0.5, (float)0.8));
//        }

//        geometry geometry = new geometry("Polygon", model);
//        feature _feature = new feature(
//                "Feature",
//                new property(),
//                new geometry("Polygon", model));
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


        System.out.println(String.format("Data sent to gateway:{%s}", gson.toJson(model)));
        producer.send(gson.toJson(model));

//        return gson.toJson(model);
    }
}
