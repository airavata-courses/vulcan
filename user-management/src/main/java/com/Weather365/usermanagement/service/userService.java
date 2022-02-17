package com.Weather365.usermanagement.service;

import com.Weather365.usermanagement.model.userRequest;
import com.Weather365.usermanagement.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Autowired
    private userRepository repository;

    public Integer register(userRequest userRequest){

        try {
            String hashPassword = new BCryptPasswordEncoder().encode(userRequest.getPassword());
            userRequest.setPassword(hashPassword);
            return this.repository.save(userRequest).getUserId();
        }
        catch (Exception ex){
            System.out.println("Exception occured in user service" + ex);
            return -1;
        }
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
            System.out.println("Exception - isValidUser" + ex);
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
