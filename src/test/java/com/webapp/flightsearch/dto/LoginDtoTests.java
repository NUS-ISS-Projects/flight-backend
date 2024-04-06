package com.webapp.flightsearch.dto;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class LoginDtoTests {

    @Test
    void givenLoginDto_whenSettersAndGettersUsed_thenCorrectValuesReturned() {
        String testUsername = "user123";
        String testPassword = "securePassword";

        LoginDto loginDto = new LoginDto();

        loginDto.setUsername(testUsername);
        loginDto.setPassword(testPassword);

        assertEquals(testUsername, loginDto.getUsername());
        assertEquals(testPassword, loginDto.getPassword());
    }
}
