package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.JourneyDetails;

public class BookmarkDto {
    private Integer id;
    private String username;
    private String tripType;
    private String noOfAdults;
    private String noOfChildren;
    private String cabinClass;
    private String route;
    private String price;
    private JourneyDetailsDto departureDetails;
    private JourneyDetailsDto returnDetails;


    public BookmarkDto(Integer id,
                       String tripType,
                       String noOfAdults,
                       String noOfChildren,
                       String cabinClass,
                       String route,
                       String price, JourneyDetails departureDetails, JourneyDetails returnDetails

    ) {
        this.id = id;
        this.tripType = tripType;
        this.noOfAdults = noOfAdults;
        this.noOfChildren = noOfChildren;
        this.cabinClass = cabinClass;
        this.route = route;
        this.price = price;
        this.departureDetails = new JourneyDetailsDto(departureDetails);
        this.returnDetails = new JourneyDetailsDto(returnDetails);
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return username;
    }

    public void setUserName(String username) {
        this.username = username;
    }

    public String getTripType() {
        return tripType;
    }

    public void setTripType(String tripType) {
        this.tripType = tripType;
    }

    public String getNoOfAdults() {
        return noOfAdults;
    }

    public void setNoOfAdults(String noOfAdults) {
        this.noOfAdults = noOfAdults;
    }

    public String getNoOfChildren() {
        return noOfChildren;
    }

    public void setNoOfChildren(String noOfChildren) {
        this.noOfChildren = noOfChildren;
    }

    public String getCabinClass() {
        return cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getRoute() {
        return route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public JourneyDetailsDto getDepartureDetails() {
        return departureDetails;
    }

    public void setDepartureDetails(JourneyDetailsDto departureDetails) {
        this.departureDetails = departureDetails;
    }

    public JourneyDetailsDto getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(JourneyDetailsDto returnDetails) {
        this.returnDetails = returnDetails;
    }

}
