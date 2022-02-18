package com.Weather365.usermanagement.controller;

import com.Weather365.usermanagement.model.loginResponse;
import com.Weather365.usermanagement.model.userRequest;
import com.Weather365.usermanagement.model.userResponse;
import com.Weather365.usermanagement.service.userService;
import com.Weather365.usermanagement.utility.utility;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @PostMapping
    @RequestMapping(value = "/register")
    public ResponseEntity<String> register(@RequestBody String user){

        //System-log
        System.out.println("Register API invoked");

        String _status = "fail";
        String _message = "Registration failed!";
        Gson gson = new Gson();
        String response = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;

        try {
            Type modelType = new TypeToken<userRequest>() {}.getType();
            userRequest data = gson.fromJson(user, modelType);

            //validate the given user email address
            if(this.utility.isValidEmail(data.getEmailId())){
                if(this.service.register(data)){
                    _status = "success";
                    _message = "User Registration Success";
                    status = HttpStatus.OK;
                    System.out.println(_message);
                }
            }
            else{
                _message = "User Registration failed : Invalid email address";
                //System-log
                System.out.println(_message);
            }
        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at register api " + ex);
        }
        response = gson.toJson(new userResponse(_status, _message));
        return new ResponseEntity<>(response, status);
    }

    @PostMapping
    @RequestMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody String loginRequest){

        //System-log
        System.out.println("Register API invoked");
        String response = null;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;


        try {
            Type modelType = new TypeToken<com.Weather365.usermanagement.model.loginRequest>() {}.getType();
            Gson gson = new Gson();
            com.Weather365.usermanagement.model.loginRequest data = gson.fromJson(loginRequest, modelType);

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
            status = HttpStatus.OK;
        }
        catch (Exception ex){
            //System-log
            System.out.println("Exception at login" + ex);
        }

        return new ResponseEntity<>(response, status);
    }
}
