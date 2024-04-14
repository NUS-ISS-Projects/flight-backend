package com.webapp.flightsearch.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.service.UserService;
import com.webapp.flightsearch.util.FirestoreRetriever;
import com.webapp.flightsearch.util.FirestoreWriter;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private AuthenticationManager authenticationManager;
    @MockBean
    private FirestoreWriter firestoreWriter;
    @MockBean
    private FirestoreRetriever firestoreRetriever;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

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
    void testLoginUser() throws Exception {
        LoginDto mockUser = new LoginDto();
        mockUser.setUsername("testUser");
        mockUser.setPassword("password");

        when(userService.loginUser(any(LoginDto.class))).thenReturn("mockToken");
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isOk());
        verify(userService).loginUser(any(LoginDto.class));

        when(userService.loginUser(any(LoginDto.class)))
                .thenThrow(new AuthenticationException("Login failed: Invalid username or password.") {
                });
        mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string(containsString("Login failed: Invalid username or password.")));
        verify(userService, times(2)).loginUser(any(LoginDto.class));
    }

    @Test
    void testRegisterUser() throws Exception {
        SignUpDto mockUser = new SignUpDto();

        User user = new User();
        when(userService.createUser(any(SignUpDto.class))).thenReturn(user);

        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isOk());
        verify(userService).createUser(any(SignUpDto.class));

        when(userService.createUser(any(SignUpDto.class))).thenThrow(new Exception());
        mockMvc.perform(post("/api/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(mockUser)))
                .andExpect(status().isBadRequest());
        verify(userService, times(2)).createUser(any(SignUpDto.class));
    }

//    @Test
//    void testGetUserProfile() throws Exception {
//        String userName = "test";
//        User user = new User();
//        when(UserService.loadUserByUsername("user")).thenReturn(user);
//
//        mockMvc.perform(get("/api/user/userProfile/{userName}", userName))
//                .andExpect(status().isOk());
//        verify(userService).loadUserByUsername("user");
//
//        when(UserService.loadUserByUsername("user")).thenThrow(new Exception());
//        mockMvc.perform(get("/api/user/userProfile/{userName}", userName))
//                .andExpect(status().isOk());
//        verify(userService, times(2)).loadUserByUsername(userName);
//    }

//    @Test
//    void testEditProfile() throws Exception {
//        String userName = "test";
//        User userDetails = new User();
//        doNothing().when(userService).editProfile(userName, userDetails);
//
//
//        mockMvc.perform(put("/api/user/editProfile/" + userName)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(asJsonString(userDetails)))
//                .andExpect(status().isOk());
//
//        verify(userService).editProfile(userName, any(User.class));
//    }
//
//    @Test
//    void testChangePassword() throws Exception {
//        User passwords = new User();  // Setup passwords
//        when(userService.changePassword(eq("user"), any(User.class))).thenReturn(null);
//
////        mockMvc.perform(put("/api/user/change-password/user")
////                        .contentType(MediaType.APPLICATION_JSON)
////                        .content)
////                .andExpect(status().isOk());
//
//        verify(userService).changePassword(eq("user"), any(User.class));
//    }

//    @Test
//    void testbookmarkFlight() throws Exception {
//        String username = "admin";
//        BookmarkDto bookmarkDto = new BookmarkDto(1,
//                "Round Trip",
//                "1",
//                "1",
//                "Economy",
//                "SIN-NRT",
//                "100",
//                null,
//                null);
//
//        doNothing().when(userService).bookmarkFlight(username, bookmarkDto);
//
//        mockMvc.perform(post("/api/user/{userName}/bookmark", username)
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(new ObjectMapper().writeValueAsString(bookmarkDto)))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("Bookmark added successfully!"))
//                .andExpect(jsonPath("$.bookmark.id").value(bookmarkDto.getId()));
//    }
//
//    @Test
//    @WithMockUser(username = "admin", roles = {"USER"})
//    void getBookmarks_ShouldReturnListOfBookmarks() throws Exception {
//        String username = "admin";
//        List<BookmarkDto> bookmarks = Arrays.asList(
//                new BookmarkDto(1, "FL123", username, username, username, username, username,
//                        null, null),
//                new BookmarkDto(2, "FL456", username, username, username, username, username,
//                        null, null));
//
//        // Mock the service call to return the list of bookmarks
//        when(userService.getFlightBookmarks(username)).thenReturn(bookmarks);
//
//        // Perform the GET request and verify the response
//        mockMvc.perform(get("/api/user/{userName}/bookmarks", username)
//                        .accept(MediaType.APPLICATION_JSON))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].id").value(bookmarks.get(0).getId()));
//    }
}
