/**
 * FlightController.java
 * 
 * This class is a part of the flightfeecomparer project.
 * It is responsible for handling HTTP requests related to flights.
 * 
 */
package com.webapp.flightsearch.controller;

import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.Flight.FlightItenerary;
import com.webapp.flightsearch.entity.Role;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.service.AmadeusConnect;
import com.webapp.flightsearch.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;

import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api")
public class FlightController {
    private final AmadeusConnect amadeusConnect;

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping("/login")
    public ResponseEntity<String> authenticateUser(@RequestBody LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager
                    .authenticate(new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String token = JwtUtil.generateJwtToken(authentication);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (AuthenticationException e) {
            return new ResponseEntity<>("Login failed: Invalid username or password.", HttpStatus.UNAUTHORIZED);
        }
    }


    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignUpDto signUpDto){
        // checking for username exists in a database

        System.out.println("the dto is "+ signUpDto);
        if(userRepository.existsByUserName(signUpDto.getUsername())){
            return new ResponseEntity<>("Username is already exist!", HttpStatus.BAD_REQUEST);
        }
        // checking for email exists in a database
        if(userRepository.existsByEmail(signUpDto.getEmail())){
            return new ResponseEntity<>("Email is already exist!", HttpStatus.BAD_REQUEST);
        }
        // creating user object
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        user.setRoles(Collections.singleton(roles));
        userRepository.save(user);
        return new ResponseEntity<>("User is registered successfully!", HttpStatus.OK);
    }

    @Autowired
    public FlightController(AmadeusConnect amadeusConnect) {
        this.amadeusConnect = amadeusConnect;
    }

    @GetMapping("/locations")
    public ResponseEntity<Location[]> locations(@RequestParam(required=true) String keyword) throws ResponseException {
        Location[] locations = this.amadeusConnect.location(keyword);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/flights")
    public ResponseEntity<FlightOfferSearch[]> flights(@RequestParam(required=true) String origin,
                                          @RequestParam(required=true) String destination,
                                          @RequestParam(required=true) String departDate,
                                          @RequestParam(required=true) String adults,
                                          @RequestParam(required = false) String returnDate)
                                          throws ResponseException {
        FlightOfferSearch[] flights = this.amadeusConnect.flights(origin, destination, departDate, adults, returnDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/itineraries")
    public ResponseEntity<List<FlightItenerary>> itineraries(@RequestParam(required=true) String origin,
                                          @RequestParam(required=true) String destination,
                                          @RequestParam(required=true) String departDate,
                                          @RequestParam(required=true) String adults,
                                          @RequestParam(required = false) String returnDate)
                                          throws ResponseException {
        List<FlightItenerary> itineraries = this.amadeusConnect.itineraries(origin, destination, departDate, adults, returnDate);
        return new ResponseEntity<>(itineraries, HttpStatus.OK);
    }
}