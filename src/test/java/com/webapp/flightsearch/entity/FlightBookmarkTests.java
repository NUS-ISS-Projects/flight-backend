package com.webapp.flightsearch.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class FlightBookmarkTests {

    private FlightBookmark flightBookmark;

    @BeforeEach
    public void setUp() {
        flightBookmark = new FlightBookmark();
    }

    @Test
    public void testGetAndSetId() {
        Integer id = 1;
        flightBookmark.setId(id);
        assertEquals(id, flightBookmark.getId());
    }

    @Test
    public void testGetAndSetUserName() {
        String userName = "testUser";
        flightBookmark.setUserName(userName);
        assertEquals(userName, flightBookmark.getUserName());
    }

    @Test
    public void testGetAndSetNoOfAdults() {
        String noOfAdults = "1";
        flightBookmark.setNoOfAdults(noOfAdults);
        assertEquals(noOfAdults, flightBookmark.getNoOfAdults());
    }

    @Test
    public void testGetAndSetNoOfChildren() {
        String noOfChildren = "1";
        flightBookmark.setNoOfChildren(noOfChildren);
        assertEquals(noOfChildren, flightBookmark.getNoOfChildren());
    }

    @Test
    public void testGetAndSetCabinClass() {
        String cabinClass = "Economy";
        flightBookmark.setCabinClass(cabinClass);
        assertEquals(cabinClass, flightBookmark.getCabinClass());
    }

    @Test
    public void testGetAndRoute() {
        String route = "SIN-NRT";
        flightBookmark.setRoute(route);
        assertEquals(route, flightBookmark.getRoute());
    }

    @Test
    public void testGetAndSetPrice() {
        String price = "888";
        flightBookmark.setPrice(price);
        assertEquals(price, flightBookmark.getPrice());
    }

    @Test
    public void testGetandSetDepartureDetails() {
        JourneyDetails departureDetails = mock(JourneyDetails.class);
        flightBookmark.setDepartureDetails(departureDetails);
        assertEquals(departureDetails, flightBookmark.getDepartureDetails());
    }

    @Test
    public void testGetandSetReturnDetails() {
        JourneyDetails returnDetails = mock(JourneyDetails.class);
        flightBookmark.setReturnDetails(returnDetails);
        assertEquals(returnDetails, flightBookmark.getReturnDetails());
    }

}
