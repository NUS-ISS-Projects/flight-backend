package com.webapp.flightsearch.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTests {

    @Test
    void givenRoleEntity_whenSettersAndGettersUsed_thenCorrectValuesReturned() {
        Integer testId = 1;
        String testName = "ROLE_ADMIN";

        Role role = new Role();

        role.setId(testId);
        role.setName(testName);

        assertEquals(testId, role.getId());
        assertEquals(testName, role.getName());
    }
}
