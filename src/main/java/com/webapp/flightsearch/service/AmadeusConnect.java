/**
 * The AmadeusConnect class provides a connection and method to the Amadeus API.
 * This class is marked as a Spring Service, meaning it is a singleton and can be autowired where needed.
 *
 */
package com.webapp.flightsearch.service;

import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.FlightOfferSearch.Itinerary;
import com.amadeus.resources.FlightOfferSearch.SearchSegment;
import com.amadeus.resources.Location;
import com.webapp.flightsearch.entity.Flight;
import com.webapp.flightsearch.entity.Flight.FlightOfferDetail;
import com.webapp.flightsearch.entity.Flight.FlightItinerary;
import com.amadeus.exceptions.ResponseException;

import java.util.ArrayList;
import java.util.List;

@Service
public class AmadeusConnect {
    private Amadeus amadeus;
    // TODO: Move to application.properties and .env for security purposes
    private AmadeusConnect() {
        this.amadeus = Amadeus
            .builder("4LKfQmyyiC6Gx0oQpAJi1SrdC2yUlOlr", "byBwbKR9KH7e4oDu")
            .build();
    }
    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
            .with("keyword", keyword)
            .and("subType", Locations.AIRPORT));
    }

    public FlightOfferSearch[] flights(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        
        FlightOfferSearch[] flightOffers;

        if (returnDate == null) {
            flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("adults", adults)
                        .and("max", 3));
        }
        else {
            flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("returnDate", returnDate)
                        .and("adults", adults)
                        .and("max", 3));
        }
        return flightOffers;
    }

    public List<FlightOfferDetail> itineraries(String origin, String destination, String departDate, String adults, String returnDate) throws ResponseException {
        FlightOfferSearch[] flightOffers;

        if (returnDate == null) {
            flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("adults", adults)
                        .and("max", 3));
        }
        else {
            flightOffers = amadeus.shopping.flightOffersSearch.get(
                Params.with("originLocationCode", origin)
                        .and("destinationLocationCode", destination)
                        .and("departureDate", departDate)
                        .and("returnDate", returnDate)
                        .and("adults", adults)
                        .and("max", 3));
        }
    
        List<FlightOfferDetail> flightOfferDetails = new ArrayList<>();
        Flight flight = new Flight();
    
        for (FlightOfferSearch offer : flightOffers) {
            FlightOfferDetail flightOfferDetail = flight.new FlightOfferDetail(offer.getId());
    
            for (Itinerary itinerary : offer.getItineraries()) {
                for (SearchSegment segment : itinerary.getSegments()) {
                    String departureTime = segment.getDeparture().getAt();
                    String departureIataCode = segment.getDeparture().getIataCode();
                    String arrivalTime = segment.getArrival().getAt();
                    String arrivalIataCode = segment.getArrival().getIataCode();
                    String airline = segment.getCarrierCode();
                    boolean isDirectFlight = segment.getNumberOfStops() == 0;
                    double totalPrice = offer.getPrice().getTotal();
                    String currency = offer.getPrice().getCurrency();
                    int bookableSeats = offer.getNumberOfBookableSeats();
                    String duration = segment.getDuration();
    
                    FlightItinerary flightItinerary = flight.new FlightItinerary(departureTime, departureIataCode, arrivalTime, arrivalIataCode, airline, isDirectFlight, totalPrice, currency, bookableSeats, duration);
                    flightOfferDetail.addItinerary(flightItinerary);
                }
            }
    
            flightOfferDetails.add(flightOfferDetail);
        }
    
        return flightOfferDetails;
    }
}