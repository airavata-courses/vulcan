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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
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

        System.out.println("Register API invoked");
        Integer retVal = -1;
        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR;
        String response = null;

        try {
            Type modelType = new TypeToken<userRequest>() {}.getType();
            Gson gson = new Gson();
            userRequest data = gson.fromJson(user, modelType);

            //validate the given user email address
            if(this.utility.isValidEmail(data.getEmailId())){

                retVal =  this.service.register(data);

                response = gson.toJson(new userResponse(
                        retVal,
                        data.getFirstName(),
                        data.getLastName(),
                        data.getEmailId()));

                status = HttpStatus.OK;
                System.out.println("User Registration Success");
            }
            else{
                System.out.println("User Registration failed : Invalid email address");
            }
        }
        catch (Exception ex){
            System.out.println("Exception at register api " + ex);
        }
        System.out.println("Registration Successful");
        return new ResponseEntity<>(response, status);
    }

    @PostMapping
    @RequestMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody String loginRequest){

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
                        userData.getEmailId(),
                        userData.getFirstName() + " " + userData.getLastName()
                );

                //login response with jwt value
                response = gson.toJson(new loginResponse(jwt));
            }
            System.out.println("Login Successful");
            status = HttpStatus.OK;
        }
        catch (Exception ex){
            System.out.println("Exception at login" + ex);
        }

        return new ResponseEntity<>(response, status);
    }


}
