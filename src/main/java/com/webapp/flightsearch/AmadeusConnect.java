/**
 * The AmadeusConnect class provides a connection and method to the Amadeus API.
 * This class is marked as a Spring Service, meaning it is a singleton and can be autowired where needed.
 *
 */
package com.webapp.flightsearch;

import org.springframework.stereotype.Service;

import com.amadeus.Amadeus;
import com.amadeus.Params;
import com.amadeus.referenceData.Locations;
import com.amadeus.resources.Location;
import com.amadeus.exceptions.ResponseException;

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
}