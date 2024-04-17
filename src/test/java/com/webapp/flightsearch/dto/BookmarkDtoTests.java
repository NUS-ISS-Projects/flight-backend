package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.JourneyDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class BookmarkDtoTests {

    private BookmarkDto bookmarkDto;
    private JourneyDetails mockDepartureDetails;
    private JourneyDetails mockReturnDetails;

    @BeforeEach
    public void setUp() {
        mockDepartureDetails = mock(JourneyDetails.class);
        mockReturnDetails = mock(JourneyDetails.class);

        bookmarkDto = new BookmarkDto(
                1,
                "business",
                "2",
                "1",
                "economy",
                "NYC-LAX",
                "$200",
                mockDepartureDetails,
                mockReturnDetails
        );
    }

    @Test
    public void testSetAndGetId() {
        bookmarkDto.setId(2);
        assertEquals(2, bookmarkDto.getId());
    }

    @Test
    public void testSetAndGetUserName() {
        bookmarkDto.setUserName("testUser");
        assertEquals("testUser", bookmarkDto.getUserName());
    }

    @Test
    public void testSetAndGetTripType() {
        bookmarkDto.setTripType("business");
        assertEquals("business", bookmarkDto.getTripType());
    }

    @Test
    public void testSetAndGetNoOfAdults() {
        bookmarkDto.setNoOfAdults("3");
        assertEquals("3", bookmarkDto.getNoOfAdults());
    }

    @Test
    public void testSetAndGetNoOfChildren() {
        bookmarkDto.setNoOfChildren("1");
        assertEquals("1", bookmarkDto.getNoOfChildren());
    }

    @Test
    public void testSetAndGetCabinClass() {
        bookmarkDto.setCabinClass("Economy");
        assertEquals("Economy", bookmarkDto.getCabinClass());
    }

    @Test
    public void testSetAndGetRoute() {
        bookmarkDto.setRoute("LAX-NYC");
        assertEquals("LAX-NYC", bookmarkDto.getRoute());
    }

    @Test
    public void testSetAndGetPrice() {
        bookmarkDto.setPrice("888");
        assertEquals("888", bookmarkDto.getPrice());
    }

    @Test
    public void testSetAndGetDepartureDetails() {
        JourneyDetailsDto newDepartureDetails = new JourneyDetailsDto(mock(JourneyDetails.class));
        bookmarkDto.setDepartureDetails(newDepartureDetails);
        assertEquals(newDepartureDetails, bookmarkDto.getDepartureDetails());
    }

    @Test
    public void testSetAndGetReturnDetails() {
        JourneyDetailsDto newReturnDetails = new JourneyDetailsDto(mock(JourneyDetails.class));
        bookmarkDto.setReturnDetails(newReturnDetails);
        assertEquals(newReturnDetails, bookmarkDto.getReturnDetails());
    }
}
