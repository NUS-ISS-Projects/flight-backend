// package com.webapp.flightsearch.util;

// import com.webapp.flightsearch.dto.BookmarkDto;
// import com.webapp.flightsearch.dto.JourneyDetailsDto;
// import com.webapp.flightsearch.dto.SegmentDto;

// import java.util.Map;

// public class BookmarkMapper {

// public static BookmarkDto mapToBookmarkDto(Map<String, Object> bookmarkData)
// {

// // Mapping departure details
// Map<String, Object> departureDetailsData = (Map<String, Object>)
// bookmarkData.get("departureDetails");
// JourneyDetailsDto departureDetails =
// mapToJourneyDetailsDto(departureDetailsData);

// // Mapping return details
// Map<String, Object> returnDetailsData = (Map<String, Object>)
// bookmarkData.get("returnDetails");
// JourneyDetailsDto returnDetails = mapToJourneyDetailsDto(returnDetailsData);

// BookmarkDto bookmarkDto = new BookmarkDto(null, null, null, null, null, null,
// null, departureDetails,
// returnDetails);

// bookmarkDto.setCabinClass((String) bookmarkData.get("cabinClass"));
// bookmarkDto.setTripType((String) bookmarkData.get("tripType"));
// bookmarkDto.setRoute((String) bookmarkData.get("route"));
// bookmarkDto.setNoOfChildren((String) bookmarkData.get("noOfChildren"));
// bookmarkDto.setPrice((String) bookmarkData.get("price"));
// bookmarkDto.setNoOfAdults((String) bookmarkData.get("noOfAdults"));
// bookmarkDto.setUserName((String) bookmarkData.get("userName")); // Assuming
// you want to set the username

// return bookmarkDto;
// }

// private static JourneyDetailsDto mapToJourneyDetailsDto(Map<String, Object>
// journeyDetailsData) {
// JourneyDetailsDto journeyDetailsDto = new JourneyDetailsDto(null);

// journeyDetailsDto.setDuration((String) journeyDetailsData.get("duration"));
// journeyDetailsDto.setDate((String) journeyDetailsData.get("date"));

// // Mapping segments
// Object segmentsObject = journeyDetailsData.get("segments");
// if (segmentsObject instanceof Iterable) {
// Iterable<Map<String, Object>> segmentsData = (Iterable<Map<String, Object>>)
// segmentsObject;
// for (Map<String, Object> segmentData : segmentsData) {
// SegmentDto segmentDto = new SegmentDto(null);
// segmentDto.setDepartureTime((String) segmentData.get("departureTime"));
// segmentDto.setTravelTime((String) segmentData.get("travelTime"));
// segmentDto.setDepartureAirport((String) segmentData.get("departureAirport"));
// segmentDto.setArrivalTime((String) segmentData.get("arrivalTime"));
// segmentDto.setCarrierCode((String) segmentData.get("carrierCode"));
// segmentDto.setAirCraftNumber((String) segmentData.get("airCraftNumber"));
// segmentDto.setId((Integer) segmentData.get("id"));
// segmentDto.setArrivalAirport((String) segmentData.get("arrivalAirport"));
// segmentDto.setFlightNumber((String) segmentData.get("flightNumber"));

// journeyDetailsDto.getSegments().add(segmentDto);
// }
// }

// return journeyDetailsDto;
// }
// }
