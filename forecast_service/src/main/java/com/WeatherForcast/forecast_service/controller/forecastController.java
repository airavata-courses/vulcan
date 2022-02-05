package com.WeatherForcast.forecast_service.controller;
import com.WeatherForcast.forecast_service.configuration.Producer;
import com.WeatherForcast.forecast_service.model.forecastModel;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.RestController;
import java.lang.reflect.Type;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@RestController
public class forecastController {

    private final Producer producer;

    @Autowired
    public forecastController(Producer producer) {
        this.producer = producer;
    }

    @KafkaListener(topics = "${topic.name.consumer}", groupId = "group_id")
    public String forecast(ConsumerRecord<String, String> payload){

        //mocking dummy value for the intensity
        Type modelType = new TypeToken<List<forecastModel>>() {}.getType();
        Gson gson = new Gson();
        List<forecastModel> model = gson.fromJson(payload.value(), modelType);

        for(forecastModel m: model){
            m.setIntensity(ThreadLocalRandom.current()
                    .nextFloat((float)0.5, (float)0.8));
        }

        producer.send(gson.toJson(model));

        return gson.toJson(model);
    }
}
