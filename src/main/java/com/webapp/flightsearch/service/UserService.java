package com.webapp.flightsearch.service;

import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.firebase.cloud.FirestoreClient;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SegmentDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.*;
import com.webapp.flightsearch.repository.FlightBookmarkRepository;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.util.FirestoreRetriever;
import com.webapp.flightsearch.util.FirestoreWriter;
import com.webapp.flightsearch.util.JwtUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    UserRepository userRepo;
    @Autowired
    private FlightBookmarkRepository flightBookmarkRepository;

    public String loginUser(LoginDto loginDto) {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        LoginDto user = retriever.getUserFromFirestore(firestore, loginDto.getUsername());
        Authentication authentication = authenticationManager
                .authenticate(
                        new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return JwtUtil.generateJwtToken(authentication);
    }

    public User createUser(SignUpDto signUpDto) throws Exception {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        DocumentSnapshot usernameSnapshot = retriever.getUserByUsernameCheck(signUpDto.getUsername()).get();
        if (usernameSnapshot.exists()) {
            throw new Exception("Username is already exist!");
        }
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        user.setRoles(Collections.singleton(roles));

        FirestoreWriter userWriter = new FirestoreWriter();
        userWriter.saveUserToFirestore(firestore, user);

        return user;
    }

    public static User loadUserByUsername(String username) throws UsernameNotFoundException {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        try {
            DocumentSnapshot documentSnapshot = retriever.getUserByUsernameCheck(username).get();
            if (documentSnapshot.exists()) {
                User user = new User();
                user.setName(documentSnapshot.getString("name"));
                user.setUserName(documentSnapshot.getString("userName"));
                user.setEmail(documentSnapshot.getString("email"));
                user.setPassword(documentSnapshot.getString("password"));
                return user;
            } else {
                throw new UsernameNotFoundException("User not found with username: " + username);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }

    public static User editProfile(String userName, User userDetails) {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        FirestoreWriter userWriter = new FirestoreWriter();
        try {
            DocumentSnapshot documentSnapshot = retriever.getUserByUsernameCheck(userName).get();
            if (documentSnapshot.exists()) {
                User updatedUser = new User();
                if (userDetails.getEmail() != null) {
                    updatedUser.setEmail(userDetails.getEmail());
                }
                if (userDetails.getName() != null) {
                    updatedUser.setName(userDetails.getName());
                }
                updatedUser.setUserName(documentSnapshot.getString("userName"));
                updatedUser.setPassword(documentSnapshot.getString("password"));
                userWriter.saveUserToFirestore(firestore, updatedUser);
                return updatedUser;
            } else {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }

    public User changePassword(String userName, User passwords) {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        FirestoreWriter userWriter = new FirestoreWriter();
        try {
            DocumentSnapshot documentSnapshot = retriever.getUserByUsernameCheck(userName).get();
            if (documentSnapshot.exists()) {
                boolean matches = passwordEncoder.matches(passwords.getOldPassword(), documentSnapshot.getString("password"));
                if (matches) {
                    User updatedUser = new User();
                    updatedUser.setName(documentSnapshot.getString("name"));
                    updatedUser.setUserName(documentSnapshot.getString("userName"));
                    updatedUser.setEmail(documentSnapshot.getString("email"));
                    updatedUser.setPassword(passwordEncoder.encode(passwords.getNewPassword()));
                    userWriter.saveUserToFirestore(firestore, updatedUser);
                    return updatedUser;
                } else {
                    throw new RuntimeException("Old password does not match.");
                }
            } else {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }


    @Transactional
    public FlightBookmark bookmarkFlight(String userName, BookmarkDto savedBookmark) {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        FirestoreWriter bookmarkWriter = new FirestoreWriter();
        try {
            DocumentSnapshot documentSnapshot = retriever.getUserByUsernameCheck(userName).get();
            if (documentSnapshot.exists()) {
                FlightBookmark bookmark = new FlightBookmark();
                bookmark.setUserName(userName);
                setFlightBookmarkDetails(bookmark, savedBookmark);
                setJourneyDetails(bookmark, savedBookmark);
                bookmarkWriter.saveBookMarkToFirestore(firestore, bookmark, savedBookmark);
                return bookmark;
            } else {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }

    public List<BookmarkDto> getFlightBookmarks(String userName) {
        Firestore firestore = FirestoreClient.getFirestore();
        FirestoreRetriever retriever = new FirestoreRetriever(firestore);
        try {
            DocumentSnapshot documentSnapshot = retriever.getUserByUsernameCheck(userName).get();
            if (documentSnapshot.exists()) {
                List<BookmarkDto> bookmarks = retriever.getBookmarks(userName);
                return bookmarks;
            } else {
                throw new UsernameNotFoundException("User not found with username: " + userName);
            }
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Failed to fetch user from Firestore", e);
        }
    }

    private void setFlightBookmarkDetails(FlightBookmark bookmark, BookmarkDto savedBookmark) {
        bookmark.setTripType(savedBookmark.getTripType());
        bookmark.setNoOfAdults(savedBookmark.getNoOfAdults());
        bookmark.setNoOfChildren(savedBookmark.getNoOfChildren());
        bookmark.setCabinClass(savedBookmark.getCabinClass());
        bookmark.setRoute(savedBookmark.getRoute());
        bookmark.setPrice(savedBookmark.getPrice());
    }

    private void setJourneyDetails(FlightBookmark bookmark, BookmarkDto savedBookmark) {
        if (savedBookmark.getDepartureDetails() != null) {
            JourneyDetails departureDetails = new JourneyDetails();
            departureDetails.setDate(savedBookmark.getDepartureDetails().getDate());
            departureDetails.setDuration(savedBookmark.getDepartureDetails().getDuration());
            if (savedBookmark.getDepartureDetails().getSegments() != null) {
                departureDetails.setSegments(convertSegmentDtosToSegments(savedBookmark.getDepartureDetails().getSegments()));
            }
            bookmark.setDepartureDetails(departureDetails);
        }
        if (savedBookmark.getReturnDetails() != null) {
            JourneyDetails returnDetails = new JourneyDetails();
            returnDetails.setDate(savedBookmark.getReturnDetails().getDate());
            returnDetails.setDuration(savedBookmark.getReturnDetails().getDuration());
            if (savedBookmark.getReturnDetails().getSegments() != null) {
                returnDetails.setSegments(convertSegmentDtosToSegments(savedBookmark.getReturnDetails().getSegments()));
            }
            bookmark.setReturnDetails(returnDetails);
        }
    }

    private List<Segment> convertSegmentDtosToSegments(List<SegmentDto> segmentDtos) {
        List<Segment> segments = new ArrayList<>();
        for (SegmentDto dto : segmentDtos) {
            Segment segment = new Segment();
            segment.setId(dto.getId());
            segment.setDepartureTime(dto.getDepartureTime());
            segment.setArrivalTime(dto.getArrivalTime());
            segment.setTravelTime(dto.getTravelTime());
            segment.setDepartureAirport(dto.getDepartureAirport());
            segment.setArrivalAirport(dto.getArrivalAirport());
            segment.setFlightNumber(dto.getFlightNumber());
            segment.setAirCraftNumber(dto.getAirCraftNumber());
            segment.setCarrierCode(dto.getCarrierCode());
            segments.add(segment);
        }
        return segments;
    }
}