package com.webapp.flightsearch;

import com.amadeus.exceptions.ResponseException;
import com.webapp.flightsearch.controller.FlightController;
import com.webapp.flightsearch.service.AmadeusConnect;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@WebMvcTest(FlightController.class)
class FlightControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AmadeusConnect amadeusConnect;

    private com.amadeus.resources.Location[] mockLocations;

    @BeforeEach
    public void setup() throws ResponseException {
        com.amadeus.resources.Location mockLocationSHA = mock(com.amadeus.resources.Location.class);
        when(mockLocationSHA.getName()).thenReturn("HONGQIAO INTL");
        when(mockLocationSHA.getIataCode()).thenReturn("SHA");

        com.amadeus.resources.Location mockLocationPU = mock(com.amadeus.resources.Location.class);
        when(mockLocationPU.getName()).thenReturn("PUDONG INTL");
        when(mockLocationPU.getIataCode()).thenReturn("PU");

        mockLocations = new com.amadeus.resources.Location[]{mockLocationSHA, mockLocationPU};
        when(amadeusConnect.location("CN")).thenReturn(mockLocations);
    }

    @Test
    public void whenCallingLocationAPIWithCountryCode_itsRespondingWithRightNumberOfAirportsAssiociated() throws Exception {
        mockMvc.perform(get("/api/v1/locations")
                .param("keyword", "CN")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()) // This will print the request and response details
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(2));
    }
}

