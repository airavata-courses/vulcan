package com.Weather365.usermanagement.utility;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.*;
import java.util.Base64;
import java.util.Date;
import java.util.regex.Pattern;

@Service
public class utility {

    @Value("${jwt.key}")
    private String secret;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    //verify if the given email id is valid
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

    //generate a jwt token with user-id, user email address and user name
    public String generateJWT(Integer userId, String emailId, String userName) {

        return Jwts.builder()
                .claim("userId",userId)
                .claim("userName", userName)
                .setSubject(emailId)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }
}
