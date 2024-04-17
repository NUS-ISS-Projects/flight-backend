package com.webapp.flightsearch.entity;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class JourneyDetailsTests {
    private JourneyDetails journeyDetails;

    @BeforeEach
    public void setUp() {
        journeyDetails = new JourneyDetails();
    }

    @Test
    public void testGetAndSetId() {
        Integer id = 1;
        journeyDetails.setId(id);
        assertEquals(id, journeyDetails.getId());
    }

    @Test
    public void testGetAndSetDuration() {
        String duration = "123";
        journeyDetails.setDuration(duration);
        assertEquals(duration, journeyDetails.getDuration());
    }

    @Test
    public void testGetAndSetDate() {
        String date = "18 Jan 24";
        journeyDetails.setDuration(date);
        assertEquals(date, journeyDetails.getDate());
    }

    @Test
    public void testGetandSetSegments() {
        List<Segment> segment = mock(List.class);
        journeyDetails.setSegments(segment);
        assertEquals(segment, journeyDetails.getSegments());
    }
}
