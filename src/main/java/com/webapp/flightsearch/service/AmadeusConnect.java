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
import com.amadeus.resources.Location;
import com.amadeus.exceptions.ResponseException;

@Service
public class AmadeusConnect {
    private Amadeus amadeus;
    // TODO: Move to application.properties and .env for security purposes
    private AmadeusConnect() {
        this.amadeus = Amadeus
            .builder("0wiqVIyMdIKWHKiHAKwj3JAsBw3UBi9q", "2kvHWWeDwQaL1zMq")
            .setHostname("production")
            .build();
    }
    public Location[] location(String keyword) throws ResponseException {
        return amadeus.referenceData.locations.get(Params
            .with("keyword", keyword)
            .and("subType", Locations.AIRPORT));
    }

    public FlightOfferSearch[] flights(String origin, String destination, String departDate, String adults,
                                       String children, String travelClass, String returnDate) throws ResponseException {
        return amadeus.shopping.flightOffersSearch.get(
                  Params.with("originLocationCode", origin)
                          .and("destinationLocationCode", destination)
                          .and("departureDate", departDate)
                          .and("returnDate", returnDate)
                          .and("adults", adults)
                          .and("children",children)
                          .and("travelClass",travelClass)
                          .and("max", 20));
    }
}