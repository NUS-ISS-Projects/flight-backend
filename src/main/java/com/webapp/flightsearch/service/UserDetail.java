package com.webapp.flightsearch.service;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.Role;
import com.webapp.flightsearch.entity.User;
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
    public void bookmarkFlight(String userName, BookmarkDto bookMark) {
        User user = userRepo.findByUserName(userName);
        userName = user.getUserName();
        FlightBookmark bookmark = new FlightBookmark();
        bookmark.setFlightNumber(bookMark.getFlightNumber());
        bookmark.setUserName(userName);
        flightBookmarkRepository.save(bookmark);
    }

    public List<BookmarkDto> getFlightBookmarks(String userName) {
        List<FlightBookmark> bookmarks = flightBookmarkRepository.findByUserName(userName);
        return bookmarks.stream()
                .map(bookmark -> new BookmarkDto(bookmark.getId(), bookmark.getFlightNumber()))
                .collect(Collectors.toList());
    }
}