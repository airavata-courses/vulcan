package com.Weather365.userhistory.controller;

import com.Weather365.userhistory.model.radarRequest;
import com.Weather365.userhistory.model.radarResponse;
import com.Weather365.userhistory.model.tokenRequest;
import com.Weather365.userhistory.service.userHistoryService;
import com.Weather365.userhistory.utility.utility;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping(value = "/api/history/")
public class userHistoryController {

    @Autowired
    private utility utility;

    @Autowired
    private userHistoryService service;

    @PostMapping
    @RequestMapping(value = "/save")
    public String save(@RequestBody String request){

        //System-log
        System.out.println("Save User History invoked");

        String _status = "fail";
        String _message = "Save user history failed";
        Gson gson = new Gson();

        try {
            Type modelType = new TypeToken<radarRequest>() {}.getType();
            radarRequest data = gson.fromJson(request, modelType);

            var claims = this.utility.getAllClaimsFromToken(data.getToken());

            if(!this.utility.isValid(claims)){
                var userId = claims.get("userId", Integer.class);

                data.setUserId(userId);

                this.service.saveHistory(data);

                _message = "User History Saved Successfully";
                _status = "Success";
            }
            else{
                _message = "Save History failed : Token expired";
                //System-log
                System.out.println(_message);
            }

        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at save user history " + ex);
        }

        return gson.toJson(new radarResponse(_status, _message, null));
    }

    @GetMapping
    @RequestMapping(value = "/get")
    public String get(@RequestBody String request){

        //System-log
        System.out.println("get user history invoked");

        String _status = "fail";
        String _message = "get user history failed";
        String _data = "";
        Gson gson = new Gson();
        List<radarRequest> result = null;

        try {
            Type modelType = new TypeToken<tokenRequest>() {}.getType();
            tokenRequest data = gson.fromJson(request, modelType);

            var claims = this.utility.getAllClaimsFromToken(data.getToken());

            if(!this.utility.isValid(claims)){
                var userId = claims.get("userId", Integer.class);

                result = this.service.getHistory(userId);


                _status = "Success";
                _message = "get user history Successful";

            }
            else{
                _message = "get user history failed : Token expired";
                //System-log
                System.out.println(_message);
            }

        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at get user history " + ex);
        }

        return gson.toJson(new radarResponse(_status, _message, result));
    }
}
