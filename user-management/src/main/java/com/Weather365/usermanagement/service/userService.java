package com.Weather365.usermanagement.service;

import com.Weather365.usermanagement.model.userRequest;
import com.Weather365.usermanagement.model.userResponse;
import com.Weather365.usermanagement.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class userService {

    private final userRepository repository;

    public userService(userRepository repository) {
        this.repository = repository;
    }

    //    @Autowired


    public boolean register(userRequest userRequest){

       boolean retVal = false;

        try {
            String hashPassword = new BCryptPasswordEncoder().encode(userRequest.getPassword());
            userRequest.setPassword(hashPassword);

            this.repository.save(userRequest);

            retVal = true;
        }
        catch (Exception ex){
            System.out.println("Exception - user service" + ex);
        }
        return retVal;
    }

    public userRequest login(String emailID, String password) {

        userRequest retVal = null;
        try {
            var user = this.getAccountByEmailId(emailID);

            if(new BCryptPasswordEncoder().matches(password, user.getPassword())){
                retVal = user;
            }

//            retVal =  password.equals(user.getPassword());
        } catch (Exception ex) {
            System.out.println("Exception - isValidUser - " + ex);
        }

        return retVal;
    }

    public userRequest getAccountByEmailId(String emailId){
        System.out.println("method - getAccountByEmailId");

        userRequest retVal = null;
        try{
            retVal = this.repository.findByEmailId(emailId);
        }
        catch (Exception ex){
            System.out.println("Exception - getAccountByEmailId" + ex);
        }

        return retVal;
    }
}
