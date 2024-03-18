package com.webapp.flightsearch.repository;

import com.webapp.flightsearch.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserNameOrEmail(String username, String email);
    Boolean existsByUserName(String userName);
    Boolean existsByEmail(String email);
    User findByUserName(String userName);
}