package com.Weather365.usermanagement;

import com.Weather365.usermanagement.model.userRequest;
import com.Weather365.usermanagement.repository.userRepository;
import com.Weather365.usermanagement.service.userService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class userServiceTest {

    private userService service;
    private userRepository repository;

    @BeforeEach
    public void setup(){
        repository = mock(userRepository.class);
        service = new userService(repository);
    }

    @Test
    public void loginValidUserTest(){

        String emailId = "bwayne@gmail.com";
        String password = "password";

        String hashPassword = new BCryptPasswordEncoder()
                .encode("password");


        userRequest userStub = new userRequest(
                "Bruce",
                "Wayne",
                emailId,
                hashPassword
        );

        when(this.repository.findByEmailId(emailId)).thenReturn(userStub);

        var result = this.service.login(emailId, password);

        assertEquals(result.getFirstName() , userStub.getFirstName());

    }

    @Test
    public void loginInValidUserTest(){

        String emailId = "bwayne@gmail.com";
        String password = "password";

        userRequest userStub = null;

        when(this.repository.findByEmailId(emailId)).thenReturn(userStub);

        var result = this.service.login(emailId, password);

        assertEquals(result , userStub);

    }

    @Test
    public void registerUserTest(){

        userRequest userStub = new userRequest(
                "James",
                "Bond",
                "jbond007@gmail.com",
                "skyfall"
        );

        when(this.repository.save(userStub)).thenReturn(userStub);

        var result = this.service.register(userStub);

        assertEquals(result , true);

    }

    @Test
    public void getUserByValidEmailId(){

        String emailId = "jbond007@gmail.com";
        userRequest userStub = new userRequest(
                "James",
                "Bond",
                emailId,
                "skyfall"
        );

        when(this.repository.findByEmailId(emailId)).thenReturn(userStub);

        var result = this.service.getAccountByEmailId(emailId);

        assertEquals(result , userStub);

    }

    @Test
    public void getUserByInvalidEmailId(){

        String emailId = "jbond007@gmail.com";
        userRequest userStub = null;

        when(this.repository.findByEmailId(emailId)).thenReturn(userStub);

        var result = this.service.getAccountByEmailId(emailId);

        assertEquals(result , userStub);

    }
}
