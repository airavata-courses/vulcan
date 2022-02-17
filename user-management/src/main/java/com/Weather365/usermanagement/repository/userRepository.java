package com.Weather365.usermanagement.repository;

import com.Weather365.usermanagement.model.userRequest;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<userRequest, Integer> {
    userRequest findByEmailId(String emailId);
}
