package com.webapp.flightsearch.entity;

import java.util.ArrayList;
import java.util.List;

import com.amadeus.resources.FlightOfferSearch;
import com.webapp.flightsearch.entity.Flight.FlightItinerary;

public class Flight {
    public class FlightItinerary {
        String departureTime;
        String departureIataCode;
        String arrivalTime;
        String arrivalIataCode;
        String airline;
        boolean isDirectFlight;
        double totalPrice;
        String currency;
        int bookableSeats;
        String duration;

        public FlightItinerary(String departureTime, String departureIataCode, String arrivalTime, String arrivalIataCode, String airline, boolean isDirectFlight, double totalPrice, String currency, int bookableSeats, String duration) {
            this.departureTime = departureTime;
            this.departureIataCode = departureIataCode;
            this.arrivalTime = arrivalTime;
            this.arrivalIataCode = arrivalIataCode;
            this.airline = airline;
            this.isDirectFlight = isDirectFlight;
            this.totalPrice = totalPrice;
            this.currency = currency;
            this.bookableSeats = bookableSeats;
            this.duration = duration;
        }
    }

    public class FlightOfferDetail {
        String offerId;
        List<FlightItinerary> itineraries;

        public FlightOfferDetail(String offerId) {
            this.offerId = offerId;
            this.itineraries = new ArrayList<>();
        }

        public void addItinerary(FlightItinerary itinerary) {
            itineraries.add(itinerary);
        }
    }
}