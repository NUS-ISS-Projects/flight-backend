package com.webapp.flightsearch.dto;

public class BookmarkDto {
    private Integer id;
    private String username;
    private String flightNumber;

    public BookmarkDto(Integer id, String flightNumber) {
        this.id = id;
        this.flightNumber = flightNumber;
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


    public void setFlightNumber(String flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getFlightNumber() {
        return flightNumber;
    }

}
