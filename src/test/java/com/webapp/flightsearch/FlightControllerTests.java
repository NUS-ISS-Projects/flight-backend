package com.webapp.flightsearch;

import com.amadeus.exceptions.ResponseException;
import com.webapp.flightsearch.service.FlightSearchService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.dto.SignUpDto;
import com.webapp.flightsearch.entity.Role;
import com.webapp.flightsearch.entity.User;
import com.webapp.flightsearch.repository.RoleRepository;
import com.webapp.flightsearch.repository.UserRepository;
import com.webapp.flightsearch.util.JwtUtil;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Optional;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.hamcrest.Matchers.containsString;

@SpringBootTest
@AutoConfigureMockMvc
class FlightControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private FlightSearchService flightSearchService;

    private com.amadeus.resources.Location[] mockLocations;
    private com.amadeus.resources.FlightOfferSearch[] mockFlightsLONToNYC;

    @MockBean
    private AuthenticationManager authenticationManager;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private RoleRepository roleRepository;

    @MockBean
    private PasswordEncoder passwordEncoder;

    private String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void setup() throws ResponseException {
        com.amadeus.resources.Location mockLocationSHA = mock(com.amadeus.resources.Location.class);
        when(mockLocationSHA.getName()).thenReturn("HONGQIAO INTL");
        when(mockLocationSHA.getIataCode()).thenReturn("SHA");

        com.amadeus.resources.Location mockLocationPU = mock(com.amadeus.resources.Location.class);
        when(mockLocationPU.getName()).thenReturn("PUDONG INTL");
        when(mockLocationPU.getIataCode()).thenReturn("PU");

        mockLocations = new com.amadeus.resources.Location[]{mockLocationSHA, mockLocationPU};
        when(flightSearchService.location("CN")).thenReturn(mockLocations);

        com.amadeus.resources.FlightOfferSearch mockFlight = mock(com.amadeus.resources.FlightOfferSearch.class);
        mockFlightsLONToNYC = new com.amadeus.resources.FlightOfferSearch[]{mockFlight};
        when(flightSearchService.flights("LON", "NYC", "2024-11-15", "3", "1", "ECONOMY", "2024-11-18")).thenReturn(mockFlightsLONToNYC);
    }

    @Test
    public void whenCallingLocationAPIWithCountryCode_itsRespondingWithRightNumberOfAirportsAssiociated() throws Exception {
        mockMvc.perform(get("/api/locations")
                .param("keyword", "CN")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // This will print the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }

    @Test
    public void whenCallingFlightsAPIWithParameters_itsRespondingWithRightInfo() throws Exception {
        mockMvc.perform(get("/api/flights")
                        .param("origin", "LON")
                        .param("destination", "NYC")
                        .param("departDate", "2024-11-15")
                        .param("adults", "3")
                        .param("children", "1")
                        .param("travelClass", "ECONOMY")
                        .param("returnDate", "2024-11-18")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // This will print the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    public void authenticateUser_Success() throws Exception {
        String username = "testUser";
        String password = "password";
        String token = "mockToken";
        Authentication mockAuthentication = mock(Authentication.class);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            .username(username)
            .password(password)
            .authorities(new SimpleGrantedAuthority("ROLE_USER"))
            .build();

        when(mockAuthentication.getPrincipal()).thenReturn(userDetails);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(mockAuthentication);

        when(JwtUtil.generateJwtToken(mockAuthentication)).thenReturn(token);

        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void authenticateUser_Failure() throws Exception {
        String username = "wrongUser";
        String password = "wrongPassword";
        LoginDto loginDto = new LoginDto();
        loginDto.setUsername(username);
        loginDto.setPassword(password);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenThrow(new BadCredentialsException("Invalid username or password"));

        mockMvc.perform(post("/api/user/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(loginDto)))
                .andExpect(status().isUnauthorized()) // Expecting HTTP 401 Unauthorized
                .andExpect(content().string(containsString("Login failed: Invalid username or password")));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    public void registerUser_Success() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("testName");
        signUpDto.setUsername("testUsername");
        signUpDto.setEmail("testEmail");
        signUpDto.setPassword("testPassword");
        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");
        adminRole.setId(null);

        when(userRepository.existsByUserName(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));

        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signUpDto)))
                .andExpect(status().isOk());
    }

    @Test
    public void registerUser_UsernameExists() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("testName");
        signUpDto.setUsername("existingUsername");
        signUpDto.setEmail("testEmail");
        signUpDto.setPassword("testPassword");

        when(userRepository.existsByUserName(signUpDto.getUsername())).thenReturn(true);

        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signUpDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username is already exist!"));
    }

    @Test
    public void registerUser_EmailExists() throws Exception {
        SignUpDto signUpDto = new SignUpDto();
        signUpDto.setName("testName");
        signUpDto.setUsername("testUsername");
        signUpDto.setEmail("existingEmail");
        signUpDto.setPassword("testPassword");

        when(userRepository.existsByUserName(signUpDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(signUpDto.getEmail())).thenReturn(true);

        mockMvc.perform(post("/api/user/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(signUpDto)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Email is already exist!"));
    }

    @Test
    public void getUserProfile_UserFound() throws Exception {
        String userName = "existingUser";
        User user = new User();
        user.setUserName(userName);

        when(userRepository.findByUserName(userName)).thenReturn(user);

        mockMvc.perform(get("/api/user/userProfile/{userName}", userName))
            .andExpect(status().isOk())
            .andExpect(content().string(user.toString()));

        verify(userRepository, times(1)).findByUserName(userName);
    }

    @Test
    public void healthCheck_ReturnsOk() throws Exception {
        mockMvc.perform(get("/api/health"))
            .andExpect(status().isOk())
            .andExpect(content().string("Health OK"));
    }
}
