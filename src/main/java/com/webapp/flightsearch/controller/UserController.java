package com.webapp.flightsearch.controller;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginDto loginDto) {
        try {
            String token = userService.loginUser(loginDto);
            return ResponseEntity.ok(token);
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body("Login failed: Invalid username or password.");
        }
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto) {
        try {
            User user = userService.createUser(signUpDto);
            return ResponseEntity.ok("User is registered successfully! " + user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/userProfile/{userName}")
    public ResponseEntity<?> getUserProfile(@PathVariable String userName) {
        try {
            User user = userService.loadUserByUsername(userName);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/editProfile/{userName}")
    public ResponseEntity<?> editProfile(@PathVariable String userName,
                                         @RequestBody User userDetails) {
        try {
            User updatedUser = userService.editProfile(userName, userDetails);
            return ResponseEntity.ok(updatedUser);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(userDetails + " Failed to edit profile");
        }
    }

    @PutMapping("/change-password/{userName}")
    public ResponseEntity<?> changePassword(@PathVariable String userName, @RequestBody User passwords) {
        try {
            User updatedUser = userService.changePassword(userName, passwords);
            return ResponseEntity.ok("Password changed successfully.");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(userName + " Failed to change Password");
        }
    }

    @PostMapping("/{userName}/bookmark")
    public ResponseEntity<Map<String, Object>> bookmarkFlight(@PathVariable String userName,
                                                              @RequestBody BookmarkDto savedBookmark) {
        Map<String, Object> response = new HashMap<>();
        try {
            FlightBookmark flightBookmark = userService.bookmarkFlight(userName, savedBookmark);
            response.put("message", "Bookmark added successfully!");
            response.put("bookmark", flightBookmark);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("message", "Error in Bookmarking");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @GetMapping("/{userName}/bookmarks")
    public ResponseEntity<List<BookmarkDto>> getBookmarks(@PathVariable String userName) {
        try {
            List<BookmarkDto> bookmarks = userService.getFlightBookmarks(userName);
            return ResponseEntity.ok(bookmarks);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }
}
