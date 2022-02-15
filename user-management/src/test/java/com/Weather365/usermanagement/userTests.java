package com.Weather365.usermanagement;

import com.Weather365.usermanagement.model.user;
import com.Weather365.usermanagement.repository.userRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import static org.junit.Assert.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class userTests {

    @Autowired
    private userRepository repository;

    @Test
    public void testRegister(){
        user data = new user(
                "James",
                "Bond",
                "jbond007@gmail.com",
                "skyfall"
        );
        user result = repository.save(data);

        assertNotNull(data);
    }
}
