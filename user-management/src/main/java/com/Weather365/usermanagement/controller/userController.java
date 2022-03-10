package com.Weather365.usermanagement.controller;

import com.Weather365.usermanagement.configuration.Producer;
import com.Weather365.usermanagement.model.loginResponse;
import com.Weather365.usermanagement.model.userRequest;
import com.Weather365.usermanagement.model.userResponse;
import com.Weather365.usermanagement.service.userService;
import com.Weather365.usermanagement.utility.utility;
import com.google.gson.reflect.TypeToken;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.web.bind.annotation.*;
import com.google.gson.Gson;
import java.lang.reflect.Type;

@RestController
@RequestMapping(value = "/api/user")
public class userController {

    @Autowired
    private userService service;

    @Autowired
    private utility utility;

    @Autowired
    private Producer producer;

    @Value("${topic.name.producer.user-register-response}")
    private String registerResponse;

    @Value ("${topic.name.producer.user-login-response}")
    private String loginResponse;

    @KafkaListener(topics = "${topic.name.consumer.user-register-request}", groupId = "group_id")
    public void register(ConsumerRecord<String, String> registerRequest){

        //System-log
        System.out.println("Register API invoked");

        String _status = "fail";
        String _message = "Registration failed!";
        Gson gson = new Gson();
        String response = null;

        try {
            Type modelType = new TypeToken<userRequest>() {}.getType();
            userRequest data = gson.fromJson(registerRequest.value(), modelType);

            if(this.service.getAccountByEmailId(data.getEmailId()) != null){
                _message = "User already exists";
            }
            else{
                //validate the given user email address
                if(this.utility.isValidEmail(data.getEmailId())){
                    if(this.service.register(data)){
                        _status = "success";
                        _message = "User Registration Success";
                        System.out.println(_message);
                    }
                }
                else{
                    _message = "User Registration failed : Invalid email address";
                    //System-log
                    System.out.println(_message);
                }
            }
        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at register api " + ex);
        }
        response = gson.toJson(new userResponse(_status, _message));
        producer.send(registerResponse, response);
    }

    @KafkaListener(topics = "${topic.name.consumer.user-login-request}", groupId = "group_id")
    public void login(ConsumerRecord<String, String> loginRequest){

        //System-log
        System.out.println("Login API invoked");
        String response = null;


        try {
            Type modelType = new TypeToken<com.Weather365.usermanagement.model.loginRequest>() {}.getType();
            Gson gson = new Gson();
            com.Weather365.usermanagement.model.loginRequest data = gson.fromJson(loginRequest.value(), modelType);

            var userData = this.service.login(data.getEmailId(), data.getPassword());
            if(userData != null){

                String jwt = this.utility.generateJWT(
                        userData.getUserId(),
                        userData.getEmailId()
                );

                //login response with jwt value
                response = gson.toJson(new loginResponse(jwt));
            }
            //System-log
            System.out.println("Login Successful");
        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at login" + ex);
        }
        producer.send(loginResponse, response);
    }
}
