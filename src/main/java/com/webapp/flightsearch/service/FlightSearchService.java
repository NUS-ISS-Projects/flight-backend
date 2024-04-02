package com.webapp.flightsearch.service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.exceptions.ResponseException;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class FlightSearchService {
    private Amadeus amadeus;

    // Update application.properties for api key and secret
    public FlightSearchService(@Value("${amadeus.api.key}") String apiKey, @Value("${amadeus.api.secret}") String apiSecret) {
        this.amadeus = Amadeus.builder(apiKey, apiSecret).build();
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
                        .and("children", children)
                        .and("travelClass", travelClass)
                        .and("currencyCode", "SGD")
                        .and("max", 10));
    }
}