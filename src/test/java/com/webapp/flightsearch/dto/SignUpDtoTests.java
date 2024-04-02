package com.webapp.flightsearch.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SignUpDtoTests {

    @Test
    void givenSignUpDto_whenSettersAndGettersUsed_thenCorrectValuesReturned() {
        String testName = "John Doe";
        String testUsername = "john.doe";
        String testEmail = "john.doe@example.com";
        String testPassword = "password123";

        SignUpDto signUpDto = new SignUpDto();

        signUpDto.setName(testName);
        signUpDto.setUsername(testUsername);
        signUpDto.setEmail(testEmail);
        signUpDto.setPassword(testPassword);

        assertEquals(testName, signUpDto.getName());
        assertEquals(testUsername, signUpDto.getUsername());
        assertEquals(testEmail, signUpDto.getEmail());
        assertEquals(testPassword, signUpDto.getPassword());
    }
}
