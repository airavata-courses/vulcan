package com.Weather365.usermanagement.utility;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.regex.Pattern;

@Service
public class utility {

    //implementation adapted from
    //https://java2blog.com/validate-email-address-in-java/
    public boolean isValidEmail(String email)
    {
        String emailRegex = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +  //part before @
                "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";

        Pattern pat = Pattern.compile(emailRegex);
        if (email == null)
            return false;
        return pat.matcher(email).matches();
    }

    //hash the given message with SHA-256 message digest algorithm
    //return the result in Base64 encoded string
    public String hash(String message){
        byte[] hashedBytes;
        String retVal = "";
        try{
            //generate random salt to be added to the message
            SecureRandom random = new SecureRandom();
            byte[] salt = new byte[16];
            random.nextBytes(salt);

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(salt);

             hashedBytes = md.digest(message.getBytes(StandardCharsets.UTF_8));
             retVal = Base64.getEncoder().encodeToString(hashedBytes);
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println("Exception thrown : " + e);
        }
        return retVal;
    }
}
