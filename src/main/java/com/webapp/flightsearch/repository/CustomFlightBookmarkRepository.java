package com.webapp.flightsearch.repository;

import com.google.cloud.firestore.Firestore;
import com.webapp.flightsearch.dto.BookmarkDto;
import com.webapp.flightsearch.entity.FlightBookmark;

public interface CustomFlightBookmarkRepository {
    FlightBookmark saveBookmarkToFirebase(FlightBookmark bookmark, BookmarkDto savedBookmark);
}
