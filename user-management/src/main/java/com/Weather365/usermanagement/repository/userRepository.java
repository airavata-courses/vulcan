package com.Weather365.usermanagement.repository;

import com.Weather365.usermanagement.model.user;
import org.springframework.data.jpa.repository.JpaRepository;

public interface userRepository extends JpaRepository<user, Integer> {
}
