package com.webapp.flightsearch.controller;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.service.UserDetail;
import com.webapp.flightsearch.util.FirestoreUserWriter;
import com.webapp.flightsearch.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.cloud.FirestoreClient;

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private UserDetail userDetail;

    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(
                            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtil.generateJwtToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Login failed: Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) throws FileNotFoundException {
        // checking for username exists in a database
        if (userRepository.existsByUserName(signUpDto.getUsername())) {
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }
        // checking for email exists in a database
        if (userRepository.existsByEmail(signUpDto.getEmail())) {
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        User user = userDetail.createUser(signUpDto);

        Firestore firestore = FirestoreClient.getFirestore(); // Obtain Firestore instance
        FirestoreUserWriter userWriter = new FirestoreUserWriter();
        userWriter.saveUserToFirestore(firestore, user);

        return ResponseEntity.ok("User is registered successfully! " + user);
    }

    @GetMapping("/userProfile/{userName}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userName) {
        User user = userRepository.findByUserName(userName);
        return new ResponseEntity<>(user.toString(), HttpStatus.OK);
    }

    @PostMapping("/{userName}/bookmark")
    public ResponseEntity<?> bookmarkFlight(@PathVariable String userName, @RequestBody BookmarkDto bookMark) {
        userDetail.bookmarkFlight(userName, bookMark);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Bookmark added successfully!");
        response.put("bookmark", bookMark);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{userName}/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getBookmarks(@PathVariable String userName) {
        List<BookmarkDto> bookmarks = userDetail.getFlightBookmarks(userName);
        return ResponseEntity.ok(bookmarks);
    }
}
