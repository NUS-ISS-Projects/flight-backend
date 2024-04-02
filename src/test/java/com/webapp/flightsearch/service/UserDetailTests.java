package com.webapp.flightsearch.service;

import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.service.UserDetail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UserDetailTests {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserDetail userDetail;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void loadUserByUsername() {
        User user = new User();
        user.setUserName("testUser");
        user.setPassword("testPassword");
        user.setRoles(Collections.emptySet());

        when(userRepository.findByUserNameOrEmail("testUser", "testUser")).thenReturn(user);

        UserDetails userDetails = userDetail.loadUserByUsername("testUser");

        assertEquals("testUser", userDetails.getUsername());
        assertEquals("testPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().isEmpty());
    }

    @Test
    void loadUserByUsername_notFound() {
        when(userRepository.findByUserNameOrEmail("testUser", "testUser")).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userDetail.loadUserByUsername("testUser"));
    }
}