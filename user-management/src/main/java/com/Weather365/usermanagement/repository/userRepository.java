package com.Weather365.usermanagement.repository;

import com.Weather365.usermanagement.model.userRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface userRepository extends JpaRepository<userRequest, Integer> {
    userRequest findByEmailId(String emailId);
}
