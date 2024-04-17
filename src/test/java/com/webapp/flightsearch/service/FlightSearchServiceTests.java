package com.webapp.flightsearch.service;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class FlightSearchServiceTests {

    @MockBean
    private FlightSearchService flightSearchService;

    private com.amadeus.resources.Location[] mockLocations;
    private com.amadeus.resources.FlightOfferSearch[] mockFlightsLONToNYC;

    @BeforeEach
    void setUp() throws ResponseException {

        Location mockLocationSHA = mock(Location.class);
        when(mockLocationSHA.getName()).thenReturn("HONGQIAO INTL");
        when(mockLocationSHA.getIataCode()).thenReturn("SHA");

        Location mockLocationPU = mock(Location.class);
        when(mockLocationPU.getName()).thenReturn("PUDONG INTL");
        when(mockLocationPU.getIataCode()).thenReturn("PU");

        mockLocations = new Location[]{mockLocationSHA, mockLocationPU};
        when(flightSearchService.location("CN")).thenReturn(mockLocations);

        FlightOfferSearch mockFlight = mock(FlightOfferSearch.class);
        mockFlightsLONToNYC = new FlightOfferSearch[]{mockFlight};
        when(flightSearchService.flights("LON", "NYC", "2024-11-15", "3", "1", "ECONOMY", "2024-11-18"))
                .thenReturn(mockFlightsLONToNYC);

    }

    @Test
    void testLocation_Success() throws ResponseException {
        Location mockLocationPU = mock(Location.class);
        mockLocations = new Location[]{mockLocationPU};
        when(flightSearchService.location("CN")).thenReturn(mockLocations);

        Location[] locations = flightSearchService.location("CN");
        assertNotNull(locations);
        assertEquals(1, locations.length);
    }

    @Test
    void testLocation_Exception() throws ResponseException {
        String keyword = "invalid";
        when(flightSearchService.location(keyword)).thenThrow(ResponseException.class);

        assertThrows(ResponseException.class, () -> flightSearchService.location(keyword));
    }

    @Test
    void testFlights_Success() throws ResponseException {
        String origin = "NYC";
        String destination = "LAX";
        String departDate = "2024-04-15";
        String returnDate = "2024-04-20";
        String adults = "1";
        String children = "0";
        String travelClass = "ECONOMY";

        FlightOfferSearch mockFlight = mock(FlightOfferSearch.class);

        FlightOfferSearch[] expectedFlights = {mockFlight};
        when(flightSearchService.flights(origin, destination, departDate, adults, children, travelClass, returnDate)).thenReturn(expectedFlights);
        FlightOfferSearch[] flights = flightSearchService.flights(origin, destination, departDate, adults, children, travelClass, returnDate);

        assertNotNull(flights);
        assertEquals(1, flights.length);
    }

    @Test
    void testFlights_Exception() throws ResponseException {
        String origin = "NYC";
        String destination = "LAX";
        when(flightSearchService.flights(origin, destination, "2024-04-15", "1", "0", "ECONOMY", "2024-04-20")).thenThrow(ResponseException.class);
        assertThrows(ResponseException.class, () -> flightSearchService.flights(origin, destination, "2024-04-15", "1", "0", "ECONOMY", "2024-04-20"));
    }
}
