package com.webapp.flightsearch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private UserService userService;


    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginDto mockUser = new LoginDto();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");

        when(userService.loginUser(any(LoginDto.class))).thenReturn("mockToken");
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isOk());
        verify(userService).loginUser(any(LoginDto.class));
    }

    @Test
    void testLoginUser_Failed() throws Exception {
        LoginDto mockUser = new LoginDto();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");

        when(userService.loginUser(any(LoginDto.class)))
                .thenThrow(new AuthenticationException("Login failed: Invalid username or password.") {
                });
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Login failed: Invalid username or password.")));
        verify(userService).loginUser(any(LoginDto.class));
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        SignUpDto mockUser = new SignUpDto();

        User user = new User();
        when(userService.createUser(any(SignUpDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isOk());
        verify(userService).createUser(any(SignUpDto.class));
    }

    @Test
    void testRegisterUser_Failed() throws Exception {
        SignUpDto mockUser = new SignUpDto();
        User user = new User();

        when(userService.createUser(any(SignUpDto.class))).thenThrow(new Exception());
        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isBadRequest());
        verify(userService).createUser(any(SignUpDto.class));
    }

    @Test
    void testGetUserProfile_Success() throws Exception {
        String userName = "testUser";
        User mockUser = new User();
        mockUser.setUserName(userName);

        when(userService.loadUserByUsername(userName)).thenReturn(mockUser);

        mockMvc.perform(get("/api/user/userProfile/{userName}", userName))
                .andExpect(status().isOk());
        verify(userService).loadUserByUsername(userName);
    }

    @Test
    void testGetUserProfile_Failed() throws Exception {
        String userName = "testUser";
        User mockUser = new User();
        mockUser.setUserName(userName);

        when(userService.loadUserByUsername(userName)).thenThrow(new UsernameNotFoundException("User not found"));
        mockMvc.perform(get("/api/user/userProfile/{userName}", userName))
                .andExpect(status().isBadRequest());
        verify(userService).loadUserByUsername(userName);
    }

    @Test
    void testEditProfile_Success() throws Exception {
        String userName = "existingUser";
        User userDetails = new User();
        userDetails.setEmail("newemail@example.com");
        userDetails.setName("New Name");

        User updatedUser = new User();
        updatedUser.setUserName(userName);
        updatedUser.setEmail(userDetails.getEmail());
        updatedUser.setName(userDetails.getName());

        when(userService.editProfile(eq(userName), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/editProfile/{userName}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDetails)))
                .andExpect(status().isOk());

        verify(userService).editProfile(eq(userName), any(User.class));

    }

    @Test
    void testEditProfile_Failed() throws Exception {
        String userName = "existingUser";
        User userDetails = new User();
        userDetails.setEmail("newemail@example.com");
        userDetails.setName("New Name");

        User updatedUser = new User();
        updatedUser.setUserName(userName);
        updatedUser.setEmail(userDetails.getEmail());
        updatedUser.setName(userDetails.getName());

        when(userService.editProfile(eq(userName), any(User.class)))
                .thenThrow(new UsernameNotFoundException("User not found with username: " + userName));
        mockMvc.perform(put("/api/user/editProfile/{userName}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(userDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("Failed to edit profile")));

        verify(userService).editProfile(eq(userName), any(User.class));

    }

    @Test
    void testChangePassword_Sucess() throws Exception {
        String userName = "user123";
        User passwordDetails = new User();
        passwordDetails.setOldPassword("oldPassword");
        passwordDetails.setNewPassword("newPassword");

        User updatedUser = new User();
        updatedUser.setUserName(userName);
        updatedUser.setPassword(passwordDetails.getNewPassword()); // Assuming password gets updated

        when(userService.changePassword(anyString(), any(User.class))).thenReturn(updatedUser);

        mockMvc.perform(put("/api/user/change-password/{userName}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string("Password changed successfully."));

        verify(userService).changePassword(eq(userName), any(User.class));
    }

    @Test
    void testChangePassword_IncorrectPassword() throws Exception {
        String userName = "user123";
        User passwordDetails = new User();
        passwordDetails.setOldPassword("incorrectOldPassword");
        passwordDetails.setNewPassword("newPassword");

        when(userService.changePassword(eq(userName), any(User.class)))
                .thenThrow(new RuntimeException("Old password does not match."));

        mockMvc.perform(put("/api/user/change-password/{userName}", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(passwordDetails)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(userName + " Failed to change Password"));

        verify(userService).changePassword(eq(userName), any(User.class));
    }

    @Test
    public void testBookmarkFlight_Success() throws Exception {
        String userName = "user1";
        BookmarkDto savedBookmark = mock(BookmarkDto.class);

        FlightBookmark flightBookmark = new FlightBookmark();
        flightBookmark.setId(1);
        flightBookmark.setUserName(userName);

        when(userService.bookmarkFlight(anyString(), any(BookmarkDto.class))).thenReturn(flightBookmark);

        mockMvc.perform(post("/api/user/{userName}/bookmark", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(savedBookmark)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Bookmark added successfully!"))
                .andExpect(jsonPath("$.bookmark.userName").value(userName));

        verify(userService).bookmarkFlight(eq(userName), any(BookmarkDto.class));
    }

    @Test
    public void testBookmarkFlight_UserNotFound() throws Exception {
        String userName = "userNotFound";
        BookmarkDto savedBookmark = mock(BookmarkDto.class);

        when(userService.bookmarkFlight(anyString(), any(BookmarkDto.class)))
                .thenThrow(new UsernameNotFoundException("User not found with username: " + userName));
        mockMvc.perform(post("/api/user/{userName}/bookmark", userName)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(savedBookmark)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Error in Bookmarking"));

        verify(userService).bookmarkFlight(eq(userName), any(BookmarkDto.class));
    }

    @Test
    public void testGetBookmarks_Success() throws Exception {
        String userName = "user123";
        List<BookmarkDto> mockBookmarks = Collections.singletonList(mock(BookmarkDto.class));
        when(userService.getFlightBookmarks(eq(userName))).thenReturn(mockBookmarks);

        mockMvc.perform(get("/api/user/{userName}/bookmarks", userName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));

        verify(userService).getFlightBookmarks(eq(userName));
    }

    @Test
    public void testGetBookmarks_UserNotFound() throws Exception {
        String userName = "nonExistentUser";
        when(userService.getFlightBookmarks(eq(userName))).thenThrow(new UsernameNotFoundException("User not found with username: " + userName));

        // When & Then
        mockMvc.perform(get("/api/user/{userName}/bookmarks", userName))
                .andExpect(status().isBadRequest());

        verify(userService).getFlightBookmarks(eq(userName));
    }
}
