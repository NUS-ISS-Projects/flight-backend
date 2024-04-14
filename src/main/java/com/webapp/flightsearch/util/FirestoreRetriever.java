package com.webapp.flightsearch.util;

import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.dto.LoginDto;
import com.webapp.flightsearch.entity.FlightBookmark;
import com.webapp.flightsearch.entity.JourneyDetails;
import com.webapp.flightsearch.entity.Segment;
import com.webapp.flightsearch.entity.User;

import java.util.ArrayList;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import com.google.cloud.firestore.DocumentReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;

public class FirestoreRetriever {
    private final Firestore firestore;

    public FirestoreRetriever(Firestore firestore) {
        this.firestore = firestore;
    }

    public List<BookmarkDto> getBookmarks(String userName) {
        try {
            DocumentSnapshot document = firestore.collection("bookmark").document(userName).get().get();
            System.out.println(document);
            Map<String, Object> documentData = document.getData();
            System.out.println(documentData);
            if (documentData != null && documentData.containsKey(userName)) {
                List<Map<String, Object>> bookmarkDataList = (List<Map<String, Object>>) documentData.get(userName);

                if (bookmarkDataList != null) {
                    List<BookmarkDto> bookmarks = new ArrayList<>();
                    for (Map<String, Object> bookmarkData : bookmarkDataList) {
                        bookmarks.add(mapToBookmarkDto(bookmarkData));
                    }
                    return bookmarks;
                }
            }
            return Collections.emptyList(); // Return an empty list if no matching data found
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }

    private BookmarkDto mapToBookmarkDto(Map<String, Object> data) {
        // Extract data from the map and construct a BookmarkDto object
        Integer id = (Integer) data.get("id");
        String tripType = (String) data.get("tripType");
        String noOfAdults = (String) data.get("noOfAdults");
        String noOfChildren = (String) data.get("noOfChildren");
        String cabinClass = (String) data.get("cabinClass");
        String route = (String) data.get("route");
        String price = (String) data.get("price");
        // Assuming getDepartureDetails and getReturnDetails return appropriate data
        // types
        // Adjust the casting as necessary
        Map<String, Object> departureDetailsMap = (Map<String, Object>) data.get("departureDetails");
        JourneyDetails departureDetails = mapToJourneyDetails(departureDetailsMap);

        // Extract and cast return details
        Map<String, Object> returnDetailsMap = (Map<String, Object>) data.get("returnDetails");
        JourneyDetails returnDetails = mapToJourneyDetails(returnDetailsMap);

        // Create and return a new BookmarkDto object
        return new BookmarkDto(id, tripType, noOfAdults, noOfChildren, cabinClass, route, price, departureDetails,
                returnDetails);
    }

    private JourneyDetails mapToJourneyDetails(Map<String, Object> detailsMap) {
        // Extract journey details fields from the map and construct a JourneyDetails
        // object
        String duration = (String) detailsMap.get("duration");
        String date = (String) detailsMap.get("date");

        // Extract and cast segments list
        List<Map<String, Object>> segmentsData = (List<Map<String, Object>>) detailsMap.get("segments");
        List<Segment> segments = segmentsData.stream()
                .map(this::mapToSegment)
                .collect(Collectors.toList());

        // Create and return a new JourneyDetails object
        JourneyDetails journeyDetails = new JourneyDetails();
        journeyDetails.setDuration(duration);
        journeyDetails.setDate(date);
        journeyDetails.setSegments(segments);

        return journeyDetails;
    }

    private Segment mapToSegment(Map<String, Object> segmentMap) {
        // Extract segment fields from the map and construct a Segment object
        String departureTime = (String) segmentMap.get("departureTime");
        String travelTime = (String) segmentMap.get("travelTime");
        String departureAirport = (String) segmentMap.get("departureAirport");
        String arrivalTime = (String) segmentMap.get("arrivalTime");
        String carrierCode = (String) segmentMap.get("carrierCode");
        String airCraftNumber = (String) segmentMap.get("airCraftNumber");
        Integer id = (Integer) segmentMap.get("id");
        String arrivalAirport = (String) segmentMap.get("arrivalAirport");
        String flightNumber = (String) segmentMap.get("flightNumber");

        // Create and return a new Segment object
        Segment segment = new Segment();
        segment.setDepartureTime(departureTime);
        segment.setTravelTime(travelTime);
        segment.setDepartureAirport(departureAirport);
        segment.setArrivalTime(arrivalTime);
        segment.setCarrierCode(carrierCode);
        segment.setAirCraftNumber(airCraftNumber);
        segment.setId(id);
        segment.setArrivalAirport(arrivalAirport);
        segment.setFlightNumber(flightNumber);

        return segment;
    }

    public LoginDto getUserFromFirestore(Firestore firestore, String userName) {
        try {
            DocumentReference userRef = firestore.collection("users").document(userName);
            DocumentSnapshot snapshot = userRef.get().get();
            if (snapshot.exists()) {
                // Convert Firestore document to User object
                LoginDto user = snapshot.toObject(LoginDto.class);
                return user;
            } else {
                // User not found
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
