package com.webapp.flightsearch.entity;

import jakarta.persistence.*;

@SuppressWarnings("ALL")
@Entity
public class FlightBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String userName;
    private String tripType;
    private String noOfAdults;
    private String noOfChildren;
    private String cabinClass;
    private String route;
    private String price;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_details_id", referencedColumnName = "id")
    private JourneyDetails departureDetails;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "return_details_id", referencedColumnName = "id")
    private JourneyDetails returnDetails;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    /* User */
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
        return this.cabinClass;
    }

    public void setCabinClass(String cabinClass) {
        this.cabinClass = cabinClass;
    }

    public String getRoute() {
        return this.route;
    }

    public void setRoute(String route) {
        this.route = route;
    }

    public String getPrice() {
        return this.price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public JourneyDetails getDepartureDetails() {
        return departureDetails;
    }

    public void setDepartureDetails(JourneyDetails departureDetails) {
        this.departureDetails = departureDetails;
    }

    public JourneyDetails getReturnDetails() {
        return returnDetails;
    }

    public void setReturnDetails(JourneyDetails returnDetails) {
        this.returnDetails = returnDetails;
    }
}
