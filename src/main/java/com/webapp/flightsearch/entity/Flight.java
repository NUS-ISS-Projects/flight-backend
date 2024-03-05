package com.webapp.flightsearch.entity;


public class Flight {
    public class FlightItenerary {
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

        public FlightItenerary(String departureTime, String departureIataCode, String arrivalTime, String arrivalIataCode, String airline, boolean isDirectFlight, double totalPrice, String currency, int bookableSeats, String duration) {
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
}