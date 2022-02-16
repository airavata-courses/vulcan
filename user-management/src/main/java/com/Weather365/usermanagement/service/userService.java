package com.Weather365.usermanagement.service;

import com.Weather365.usermanagement.model.user;
import com.Weather365.usermanagement.repository.userRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class userService {

    @Autowired
    private userRepository repository;

    public Integer register(user user){

        try {
            String hashPassword = new BCryptPasswordEncoder().encode(user.getPassword());
            user.setPassword(hashPassword);
            return this.repository.save(user).getUserId();
        }
        catch (Exception ex){
            System.out.println("Exception occured in user service" + ex);
            return -1;
        }
    }

    public boolean login(String emailID, String password) {

        boolean retVal = false;
        try {
            var user = this.repository.findByEmailId(emailID);

            retVal = new BCryptPasswordEncoder().matches(password, user.getPassword());
//            retVal =  password.equals(user.getPassword());
        } catch (Exception ex) {
            System.out.println("Exception occured in user service - isValidUser" + ex);
        }

        return retVal;
    }

}
