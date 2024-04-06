package com.webapp.flightsearch.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import java.util.HashSet;
import java.util.Set;

class UserTests {

    @Test
    void givenUserEntity_whenSettersAndGettersUsed_thenCorrectValuesReturned() {
        Integer testId = 1;
        String testName = "John Doe";
        String testUsername = "john.doe";
        String testEmail = "john.doe@example.com";
        String testPassword = "securePassword123";
        Role testRole = new Role();
        testRole.setId(1);
        testRole.setName("ROLE_USER");
        Set<Role> testRoles = new HashSet<>();
        testRoles.add(testRole);

        User user = new User();

        user.setId(testId);
        user.setName(testName);
        user.setUserName(testUsername);
        user.setEmail(testEmail);
        user.setPassword(testPassword);
        user.setRoles(testRoles);

        assertEquals(testId, user.getId());
        assertEquals(testName, user.getName());
        assertEquals(testUsername, user.getUserName());
        assertEquals(testEmail, user.getEmail());
        assertEquals(testPassword, user.getPassword());
        assertEquals(testRoles, user.getRoles());
    }
}
