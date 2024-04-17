package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.JourneyDetails;
import com.webapp.flightsearch.entity.Segment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JourneyDetailsDtoTests {
    private JourneyDetails journeyDetails;
    private JourneyDetailsDto journeyDetailsDto;

    @BeforeEach
    void setUp() {
        // Mock the Segment and JourneyDetails
        Segment segment = mock(Segment.class);
        when(segment.getDepartureTime()).thenReturn("10:00 AM");
        when(segment.getArrivalTime()).thenReturn("1:00 PM");

        List<Segment> segments = Arrays.asList(segment);

        journeyDetails = mock(JourneyDetails.class);
        when(journeyDetails.getDuration()).thenReturn("3 hours");
        when(journeyDetails.getDate()).thenReturn("2024-04-15");
        when(journeyDetails.getSegments()).thenReturn(segments);

        // Initialize JourneyDetailsDto using the mocked JourneyDetails
        journeyDetailsDto = new JourneyDetailsDto(journeyDetails);
    }

    @Test
    void testSetAndGetDuration() {
        journeyDetailsDto.setDuration("5 hours");
        assertEquals("5 hours", journeyDetailsDto.getDuration());
    }

    @Test
    void testSetAndGetDate() {
        journeyDetailsDto.setDate("2024-04-20");
        assertEquals("2024-04-20", journeyDetailsDto.getDate());
    }

    @Test
    void testSetAndGetSegments() {
        SegmentDto newSegmentDto = new SegmentDto(mock(Segment.class));
        List<SegmentDto> newSegments = Arrays.asList(newSegmentDto);
        journeyDetailsDto.setSegments(newSegments);
        assertEquals(newSegments, journeyDetailsDto.getSegments());
        assertEquals(1, journeyDetailsDto.getSegments().size());
    }
}
