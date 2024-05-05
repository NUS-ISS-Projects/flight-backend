package com.webapp.flightsearch.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.FlightBookmarkRepository;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.util.FirestoreRetriever;
import com.webapp.flightsearch.util.FirestoreWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

class UserServiceTests {

    @Mock
    private Firestore mockFirestore;
    @Mock
    private CollectionReference mockCollectionReference;
    @Mock
    private FlightBookmarkRepository flightBookmarkRepository;

    @Mock
    private DocumentReference mockDocumentReference;

    @Mock
    private ApiFuture<DocumentSnapshot> mockApiFuture;

    @Mock
    private DocumentSnapshot mockDocumentSnapshot;

    private PasswordEncoder passwordEncoder;
    private FirestoreRetriever firestoreRetriever;
    private FirestoreWriter firestoreWriter;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        when(mockFirestore.collection("users")).thenReturn(mockCollectionReference);
        when(mockCollectionReference.document(anyString())).thenReturn(mockDocumentReference);
        when(mockDocumentReference.get()).thenReturn(mockApiFuture);
        when(mockApiFuture.get()).thenReturn(mockDocumentSnapshot);

        firestoreRetriever = new FirestoreRetriever(mockFirestore);
        firestoreWriter = mock(FirestoreWriter.class);
        passwordEncoder = mock(PasswordEncoder.class);
        RoleRepository roleRepository = mock(RoleRepository.class);
        userService = new UserService(passwordEncoder, roleRepository, firestoreRetriever, firestoreWriter);
    }

    @Test
    void testLoginUser_Success() throws Exception {
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername("testUser");
        loginDto.setPassword("password");

        UserDetails principal = mock(UserDetails.class);
        when(principal.getUsername()).thenReturn(loginDto.getUsername());
        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(principal, null);

        when(mockDocumentReference.get().get()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(firestoreRetriever.getUserFromFirestore(anyString())).thenReturn(loginDto);

        String result = userService.loginUser(loginDto);
        assertNotNull(result);
    }

    @Test
    void testLoginUser_Failure() throws Exception {
        LoginDto loginDto = new LoginDto();

        when(mockDocumentReference.get().get()).thenReturn(mockDocumentSnapshot);
        when(mockDocumentSnapshot.exists()).thenReturn(false);

        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.loginUser(loginDto);
        });
        assertEquals("Failed to fetch user from Firestore", exception.getMessage());
    }

    @Test
    void testCreateUser_Success() throws Exception {
        String userName = "testUser";
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername(userName);
        signUpDto.setName("Test Name");
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("123456");
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("123456");
        doNothing().when(firestoreWriter).saveUserToFirestore(any(User.class));

        User user = userService.createUser(signUpDto);
        assertEquals("testUser", user.getUserName());
        assertEquals("Test Name", user.getName());
        assertEquals("test@example.com", user.getEmail());
        assertEquals("123456", user.getPassword());
    }

    @Test
    void testCreateUser_UsernameExists() {
        String userName = "testUser";
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setUsername(userName);
        signUpDto.setName("Test Name");
        signUpDto.setEmail("test@example.com");
        signUpDto.setPassword("123456");
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);

        Exception exception = assertThrows(Exception.class, () -> userService.createUser(signUpDto));

        assertEquals("Username is already exist!", exception.getMessage());
    }

    @Test
    void testLoadUserByUsername_Success() {
        String username = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);

        when(mockDocumentSnapshot.getString("name")).thenReturn("John Doe");
        when(mockDocumentSnapshot.getString("userName")).thenReturn(username);
        when(mockDocumentSnapshot.getString("email")).thenReturn("john.doe@example.com");
        when(mockDocumentSnapshot.getString("password")).thenReturn("password");

        User user = userService.loadUserByUsername(username);
        assertNotNull(user);
        assertEquals("John Doe", user.getName());
        assertEquals(username, user.getUserName());
        assertEquals("john.doe@example.com", user.getEmail());
        assertEquals("password", user.getPassword());
    }

    @Test
    void testLoadUserByUsername_Failure() {
        String username = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.loadUserByUsername(username);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
    }

    @Test
    void testEditProfile_Success() {
        String userName = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getString("userName")).thenReturn(userName);
        when(mockDocumentSnapshot.getString("password")).thenReturn("password");
        doNothing().when(firestoreWriter).saveUserToFirestore(any(User.class));

        User userDetails = new User();
        userDetails.setName("New Name");
        userDetails.setEmail("new.email@example.com");

        User updatedUser = userService.editProfile(userName, userDetails);

        assertNotNull(updatedUser);
        assertEquals("New Name", updatedUser.getName());
        assertEquals("new.email@example.com", updatedUser.getEmail());
        assertEquals(userName, updatedUser.getUserName());
    }

    @Test
    void testEditProfile_Failure() {
        String username = "testUser";
        User userDetails = new User();
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.editProfile(username, userDetails);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
    }

    @Test
    void testChangePassword_Success() {
        String userName = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getString("name")).thenReturn("testName");
        when(mockDocumentSnapshot.getString("userName")).thenReturn(userName);
        when(mockDocumentSnapshot.getString("email")).thenReturn("test@gmail.com");
        when(mockDocumentSnapshot.getString("password")).thenReturn("oldPassword");
        when(passwordEncoder.encode(anyString())).thenReturn("123456");

        User passwords = new User();
        passwords.setOldPassword("oldPassword");
        passwords.setNewPassword("newPassword");

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        doNothing().when(firestoreWriter).saveUserToFirestore(any(User.class));
        User updatedUser = userService.changePassword(userName, passwords);
        assertNotNull(updatedUser);
        assertEquals(userName, updatedUser.getUserName());
        assertEquals("123456", updatedUser.getPassword());
    }

    @Test
    void testChangePassword_Failure_UserNotFound() {
        String username = "testUser";
        User userDetails = new User();
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.changePassword(username, userDetails);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
    }

    @Test
    void testChangePassword_Failure_NotMatch() {
        String userName = "testUser";
        User userDetails = new User();
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);
        when(mockDocumentSnapshot.getString("password")).thenReturn("oldPassword");

        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);
        Exception exception = assertThrows(RuntimeException.class, () -> {
            userService.changePassword(userName, userDetails);
        });
        assertEquals("Old password does not match.", exception.getMessage());
    }

    @Test
    void testBookmarkFlight_Success() {
        String userName = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);

        BookmarkDto savedBookmark = mock(BookmarkDto.class);
        FlightBookmark bookmark = userService.bookmarkFlight(userName, savedBookmark);
        assertNotNull(bookmark);
    }

    @Test
    void testBookmarkFlight_UserNotFound() {
        String username = "testUser";
        BookmarkDto savedBookmark = mock(BookmarkDto.class);
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.bookmarkFlight(username, savedBookmark);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
    }

    @Test
    void testGetFlightBookmarks_UserNotFound() {
        String username = "testUser";
        when(firestoreRetriever.getUserByUsernameCheck(username)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(false);
        Exception exception = assertThrows(UsernameNotFoundException.class, () -> {
            userService.getFlightBookmarks(username);
        });
        assertEquals("User not found with username: " + username, exception.getMessage());
    }

    @Test
    void testFindBookmarkByUserName_Success() {
        // Mock data
        String userName = "testUser";
        List<FlightBookmark> expectedBookmarks = new ArrayList<>();

        // Mock behavior
        when(flightBookmarkRepository.findByUserName(userName)).thenReturn(expectedBookmarks);

        when(firestoreRetriever.getUserByUsernameCheck(userName)).thenReturn(mockApiFuture);
        when(mockDocumentSnapshot.exists()).thenReturn(true);

        BookmarkDto savedBookmark = mock(BookmarkDto.class);
        List<BookmarkDto> bookmark = userService.getFlightBookmarks(userName);

        // Assertions
        assertEquals(expectedBookmarks.size(), bookmark.size());
        // Add more assertions as needed
    }
}