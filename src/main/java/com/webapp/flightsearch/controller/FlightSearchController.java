package com.webapp.flightsearch.controller;

import com.amadeus.exceptions.ResponseException;
import com.amadeus.resources.FlightOfferSearch;
import com.amadeus.resources.Location;
import com.webapp.flightsearch.service.FlightSearchService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(maxAge = 3600)
@RestController
@RequestMapping("/api")
public class FlightSearchController {
    private final FlightSearchService flightSearchService;

    public FlightSearchController(FlightSearchService flightSearchService) {
        this.flightSearchService = flightSearchService;
    }

    @GetMapping("/locations")
    public ResponseEntity<Location[]> locations(@RequestParam(required = true) String keyword) throws ResponseException {
        Location[] locations = this.flightSearchService.location(keyword);
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @GetMapping("/flights")
    public ResponseEntity<FlightOfferSearch[]> flights(
            @RequestParam(required = true) String origin,
            @RequestParam(required = true) String destination,
            @RequestParam(required = true) String departDate,
            @RequestParam(required = true) String adults,
            @RequestParam(required = false) String children,
            @RequestParam(required = false) String travelClass,
            @RequestParam(required = false) String returnDate)
            throws ResponseException {
        FlightOfferSearch[] flights = this.flightSearchService.flights(origin, destination, departDate, adults, children, travelClass, returnDate);
        return new ResponseEntity<>(flights, HttpStatus.OK);
    }

    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Health OK", HttpStatus.OK);
    }
}