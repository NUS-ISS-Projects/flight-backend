package com.webapp.flightsearch.service;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.Role;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.FlightBookmarkRepository;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class UserDetailTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @InjectMocks
    private UserDetail userDetail;

    @Mock
    private FlightBookmarkRepository flightBookmarkRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

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

    @Test
    void bookmarkFlight_ShouldCreateAndSaveBookmark() {
        // Arrange
        String userName = "testUser";
        BookmarkDto bookmarkDto = new BookmarkDto(null, "FL123");
        User user = new User();
        user.setUserName(userName);

        when(userRepository.findByUserName(userName)).thenReturn(user);

        // Act
        userDetail.bookmarkFlight(userName, bookmarkDto);

        // Assert
        verify(userRepository, times(1)).findByUserName(userName);
        verify(flightBookmarkRepository, times(1)).save(any(FlightBookmark.class));
    }

    @Test
    void getFlightBookmarks_ShouldReturnBookmarkDtoList() {
        // Arrange
        String userName = "testUser";
        FlightBookmark flightBookmark1 = new FlightBookmark();
        FlightBookmark flightBookmark2 = new FlightBookmark();

        flightBookmark1.setId(1);
        flightBookmark1.setFlightNumber("FL123");
        flightBookmark1.setUserName(userName);
        flightBookmark2.setId(2);
        flightBookmark2.setFlightNumber("FL456");
        flightBookmark2.setUserName(userName);

        List bookmarks = Arrays.asList(
            flightBookmark1,
            flightBookmark2
        );

        when(flightBookmarkRepository.findByUserName(userName)).thenReturn(bookmarks);

        // Act
        List<BookmarkDto> result = userDetail.getFlightBookmarks(userName);

        // Assert
        verify(flightBookmarkRepository, times(1)).findByUserName(userName);
        assertEquals(2, result.size());
        assertEquals("FL123", result.get(0).getFlightNumber());
        assertEquals("FL456", result.get(1).getFlightNumber());
    }

    @Test
    void createUser_ShouldSaveUserWithEncodedPasswordAndRole() {
        // Arrange
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("John Doe");
        signUpDto.setUsername("johndoe");
        signUpDto.setEmail("john.doe@example.com");
        signUpDto.setPassword("password");

        Role roleAdmin = new Role();
        roleAdmin.setName("ROLE_ADMIN");

        when(passwordEncoder.encode(signUpDto.getPassword())).thenReturn("encodedPassword");
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(roleAdmin));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User savedUser = userDetail.createUser(signUpDto);

        // Assert
        verify(passwordEncoder, times(1)).encode(signUpDto.getPassword());
        verify(roleRepository, times(1)).findByName("ROLE_ADMIN");
        verify(userRepository, times(1)).save(any(User.class));
        
        assertNotNull(savedUser);
        assertEquals(signUpDto.getName(), savedUser.getName());
        assertEquals(signUpDto.getUsername(), savedUser.getUserName());
        assertEquals(signUpDto.getEmail(), savedUser.getEmail());
        assertEquals("encodedPassword", savedUser.getPassword());
        assertTrue(savedUser.getRoles().contains(roleAdmin));
    }

}