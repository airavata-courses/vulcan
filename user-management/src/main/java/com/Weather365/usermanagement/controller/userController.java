package com.Weather365.usermanagement.controller;

import com.Weather365.usermanagement.model.user;
import com.Weather365.usermanagement.service.userService;
import com.Weather365.usermanagement.utility.utility;
import com.google.gson.reflect.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
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
    public Integer register(@RequestBody String user){

        System.out.println("Register API invoked");
        System.out.println("request - " + user);
        Integer retVal = -1;

        try {
            Type modelType = new TypeToken<user>() {}.getType();
            Gson gson = new Gson();
            user data = gson.fromJson(user, modelType);

            //validate the given user email address
            if(this.utility.isValidEmail(data.getEmailId())){

                //hash password before storing it to database
                String hashMessage = this.utility.hash(data.getPassword());
                data.setPassword(hashMessage);

                retVal =  this.service.register(data);
                System.out.println("User Registration Success");
            }
            else{
                System.out.println("User Registration failed : Invalid email address");
            }
        }
        catch (Exception ex){
            System.out.println("Exception at regsiter api " + ex);
        }

        return retVal;
    }

}
