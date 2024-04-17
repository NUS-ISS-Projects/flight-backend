package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class SegmentDtoTests {
    private SegmentDto segmentDto;

    @BeforeEach
    void setUp() {
        Segment segment = new Segment();
        segment.setId(1);
        segment.setDepartureTime("10:00 AM");
        segment.setArrivalTime("2:00 PM");
        segment.setTravelTime("4 hours");
        segment.setDepartureAirport("JFK");
        segment.setArrivalAirport("LAX");
        segment.setFlightNumber("AA100");
        segment.setAirCraftNumber("A320");
        segment.setCarrierCode("AA");

        segmentDto = new SegmentDto(segment);
    }

    @Test
    void testSetAndGetId() {
        segmentDto.setId(100);
        assertEquals(100, segmentDto.getId());
    }

    @Test
    void testSetAndGetDepartureTime() {
        segmentDto.setDepartureTime("11:00 AM");
        assertEquals("11:00 AM", segmentDto.getDepartureTime());
    }

    @Test
    void testSetAndGetArrivalTime() {
        segmentDto.setArrivalTime("3:00 PM");
        assertEquals("3:00 PM", segmentDto.getArrivalTime());
    }

    @Test
    void testSetAndGetTravelTime() {
        segmentDto.setTravelTime("5 hours");
        assertEquals("5 hours", segmentDto.getTravelTime());
    }

    @Test
    void testSetAndGetDepartureAirport() {
        segmentDto.setDepartureAirport("EWR");
        assertEquals("EWR", segmentDto.getDepartureAirport());
    }

    @Test
    void testSetAndGetArrivalAirport() {
        segmentDto.setArrivalAirport("SFO");
        assertEquals("SFO", segmentDto.getArrivalAirport());
    }

    @Test
    void testSetAndGetFlightNumber() {
        segmentDto.setFlightNumber("UA200");
        assertEquals("UA200", segmentDto.getFlightNumber());
    }

    @Test
    void testSetAndGetAirCraftNumber() {
        segmentDto.setAirCraftNumber("737MAX");
        assertEquals("737MAX", segmentDto.getAirCraftNumber());
    }

    @Test
    void testSetAndGetCarrierCode() {
        segmentDto.setCarrierCode("UA");
        assertEquals("UA", segmentDto.getCarrierCode());
    }

}
