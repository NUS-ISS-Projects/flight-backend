package com.webapp.flightsearch.service;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.SegmentDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.*;
import com.webapp.flightsearch.repository.FlightBookmarkRepository;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserDetail implements UserDetailsService {
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    UserRepository userRepo;
    @Autowired
    private FlightBookmarkRepository flightBookmarkRepository;

    public User createUser(SignUpDto signUpDto) {
        User user = new User();
        user.setName(signUpDto.getName());
        user.setUserName(signUpDto.getUsername());
        user.setEmail(signUpDto.getEmail());
        user.setPassword(passwordEncoder.encode(signUpDto.getPassword()));
        Role roles = roleRepository.findByName("ROLE_ADMIN").orElse(null);
        user.setRoles(Collections.singleton(roles));
        return userRepo.save(user);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserNameOrEmail(username, username);
        if (user == null) {
            throw new UsernameNotFoundException("User not exists by Username");
        }

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());
        return new org.springframework.security.core.userdetails.User(username, user.getPassword(), authorities);
    }

    @Transactional
    public FlightBookmark bookmarkFlight(String userName, BookmarkDto savedBookmark) {
        //toDo: findByUserName must check the DB
        //User user = userRepo.findByUserName(userName);
        //userName = user.getUserName();
        FlightBookmark bookmark = new FlightBookmark();
        bookmark.setUserName(userName);
        setFlightBookmarkDetails(bookmark, savedBookmark);
        setJourneyDetails(bookmark, savedBookmark);
        return flightBookmarkRepository.save(bookmark);
    }

    public List<BookmarkDto> getFlightBookmarks(String userName) {
        List<FlightBookmark> bookmarks = flightBookmarkRepository.findByUserName(userName);
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDto(
                        bookmark.getId(),
                        bookmark.getTripType(),
                        bookmark.getNoOfAdults(),
                        bookmark.getNoOfChildren(),
                        bookmark.getCabinClass(),
                        bookmark.getRoute(),
                        bookmark.getPrice(),
                        bookmark.getDepartureDetails(),
                        bookmark.getReturnDetails()
                ))
                .collect(Collectors.toList());
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