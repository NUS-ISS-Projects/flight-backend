package com.webapp.flightsearch.dto;

import com.webapp.flightsearch.entity.Segment;

public class SegmentDto {
    private Integer id;
    private String departureTime;
    private String arrivalTime;
    private String travelTime;
    private String departureAirport;
    private String arrivalAirport;
    private String flightNumber;
    private String airCraftNumber;
    private String carrierCode;

    public SegmentDto(Segment segment) {
        this.departureTime = segment.getDepartureTime();
        this.arrivalTime = segment.getArrivalTime();
        this.travelTime = segment.getTravelTime();
        this.departureAirport = segment.getDepartureAirport();
        this.arrivalAirport = segment.getArrivalAirport();
        this.flightNumber = segment.getFlightNumber();
        this.airCraftNumber = segment.getAirCraftNumber();
        this.carrierCode = segment.getCarrierCode();
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDepartureTime() {
        return departureTime;
    }

    public void setDepartureTime(String departureTime) {
        this.departureTime = departureTime;
    }

    public String getArrivalTime() {
        return arrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        this.arrivalTime = arrivalTime;
    }

    public String getTravelTime() {
        return travelTime;
    }

    public void setTravelTime(String travelTime) {
        this.travelTime = travelTime;
    }

    public String getDepartureAirport() {
        return departureAirport;
    }

    public void setDepartureAirport(String departureAirport) {
        this.departureAirport = departureAirport;
    }

    public String getArrivalAirport() {
        return arrivalAirport;
    }

    public void setArrivalAirport(String arrivalAirport) {
        this.arrivalAirport = arrivalAirport;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getAirCraftNumber() {
        return airCraftNumber;
    }

    public void setAirCraftNumber(String airCraftNumber) {
        this.airCraftNumber = airCraftNumber;
    }

    public String getCarrierCode() {
        return carrierCode;
    }

    public void setCarrierCode(String carrierCode) {
        this.carrierCode = carrierCode;
    }
}
