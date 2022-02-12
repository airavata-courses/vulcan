package com.Weather365.usermanagement.service;

import com.Weather365.usermanagement.model.user;
import com.Weather365.usermanagement.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Autowired
    private userRepository repository;

    public Integer register(user user){

        try {
            return this.repository.save(user).getUserId();
        }
        catch (Exception ex){
            System.out.println("Exception occured in user service" + ex);
            return -1;
        }
    }
}
