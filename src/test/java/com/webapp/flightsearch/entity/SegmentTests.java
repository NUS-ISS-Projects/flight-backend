package com.webapp.flightsearch.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SegmentTests {
    private Segment segment;

    @BeforeEach
    public void setUp() {
        segment = new Segment();
    }

    @Test
    public void testGetAndSetId() {
        Integer id = 1;
        segment.setId(id);
        assertEquals(id, segment.getId());
    }

    @Test
    public void testGetAndSetDepartureTime() {
        String departureTime = "123";
        segment.setDepartureTime(departureTime);
        assertEquals(departureTime, segment.getDepartureTime());
    }

    @Test
    public void testGetAndSetArrivalTime() {
        String arrivalTime = "456";
        segment.setArrivalTime(arrivalTime);
        assertEquals(arrivalTime, segment.getArrivalTime());
    }

    @Test
    public void testGetAndSetTravelTime() {
        String travelTime = "2 Hours";
        segment.setTravelTime(travelTime);
        assertEquals(travelTime, segment.getTravelTime());
    }

    @Test
    public void testGetAndSetDeperatureAirport() {
        String departureAirport = "SIN";
        segment.setDepartureAirport(departureAirport);
        assertEquals(departureAirport, segment.getDepartureAirport());
    }

    @Test
    public void testGetAndSetArrivalAirport() {
        String arrivalAirport = "NRT";
        segment.setArrivalAirport(arrivalAirport);
        assertEquals(arrivalAirport, segment.getArrivalAirport());
    }

    @Test
    public void testGetAndSetFlightNumber() {
        String flightNumber = "888";
        segment.setFlightNumber(flightNumber);
        assertEquals(flightNumber, segment.getFlightNumber());
    }

    @Test
    public void testGetAndSetAircraftNumber() {
        String airCraftNumber = "999";
        segment.setAirCraftNumber(airCraftNumber);
        assertEquals(airCraftNumber, segment.getAirCraftNumber());
    }

    @Test
    public void testGetAndSetCarrierCode() {
        String carrierCode = "555";
        segment.setCarrierCode(carrierCode);
        assertEquals(carrierCode, segment.getCarrierCode());
    }

}
