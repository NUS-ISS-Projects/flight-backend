package com.webapp.flightsearch.repository;

import com.webapp.flightsearch.entity.FlightBookmark;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FlightBookmarkRepository extends JpaRepository<FlightBookmark, Integer> {

    List<FlightBookmark> findByUserName(String userName);
}